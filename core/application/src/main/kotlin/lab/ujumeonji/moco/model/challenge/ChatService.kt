package lab.ujumeonji.moco.model.challenge

import lab.ujumeonji.moco.adapter.ChatSessionRepositoryAdapter
import lab.ujumeonji.moco.model.user.UserService
import lab.ujumeonji.moco.service.challenge.io.ChallengeChatInput
import lab.ujumeonji.moco.service.challenge.io.ChallengeChatOutput
import lab.ujumeonji.moco.support.error.BusinessException
import lab.ujumeonji.moco.support.prompt.PromptTemplateService
import org.slf4j.LoggerFactory
import org.springframework.ai.chat.client.ChatClient
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import java.time.LocalDateTime
import javax.validation.Valid

@Service
@Validated
class ChatService(
    private val chatSessionRepositoryAdapter: ChatSessionRepositoryAdapter,
    private val challengeService: ChallengeService,
    @Qualifier("tutorChatClient") private val tutorChatClient: ChatClient,
    @Qualifier("scoringChatClient") private val scoringChatClient: ChatClient,
    private val userService: UserService,
    private val promptTemplateService: PromptTemplateService,
) {
    private val logger = LoggerFactory.getLogger(ChatService::class.java)

    fun processChat(
        challengeId: String,
        userId: String,
        @Valid request: ChallengeChatInput,
    ): ChallengeChatOutput {
        try {
            val challenge =
                challengeService.findById(challengeId)
                    ?: throw BusinessException.challengeNotFound(challengeId)

            val user =
                userService.findById(userId)
                    ?: throw BusinessException.userNotFound(userId)

            val session =
                chatSessionRepositoryAdapter.findByChallengeIdAndUserId(challengeId, userId)
                    ?: ChatSession.create(user, challenge.id)

            if (session.remainingInteractions <= 0) {
                throw BusinessException.invalidChatRequest("채팅 제한 횟수에 도달했습니다.")
            }

            val now = LocalDateTime.now()
            session.addUserMessage(request.message, now)

            val tutorResponse =
                generateTutorResponse(
                    session = session,
                    challengeTitle = challenge.title,
                    challengeDescription = challenge.description,
                    userMessage = request.message,
                )

            session.addSystemMessage(tutorResponse, now)

            if (session.isLastInteraction) {
                session.understandingScore = calculateUnderstandingScore(session.messages)
            }

            val savedSession = chatSessionRepositoryAdapter.save(session)
            return savedSession.toResponseDto()
        } catch (e: BusinessException) {
            throw e
        } catch (e: Exception) {
            logger.error("Error processing chat for challenge $challengeId and user $userId", e)
            throw BusinessException.chatProcessingFailed("채팅 처리 중 오류가 발생했습니다")
        }
    }

    fun getChatSessionsByChallengeAndUser(
        challengeId: String,
        userId: String,
    ): ChallengeChatOutput {
        try {
            challengeService.findById(challengeId)
                ?: throw BusinessException.challengeNotFound(challengeId)

            val session = chatSessionRepositoryAdapter.findByChallengeIdAndUserId(challengeId, userId)

            return session?.toResponseDto() ?: ChallengeChatOutput.emptyOutput(
                challengeId = challengeId,
                userId = userId,
            )
        } catch (e: BusinessException) {
            throw e
        } catch (e: Exception) {
            logger.error("Error getting chat sessions for challenge $challengeId and user $userId", e)
            throw BusinessException.chatProcessingFailed("채팅 세션 조회 중 오류가 발생했습니다")
        }
    }

    @Retryable(
        value = [Exception::class],
        maxAttempts = 3,
        backoff = Backoff(delay = 1000, multiplier = 2.0),
    )
    private fun generateTutorResponse(
        session: ChatSession,
        challengeTitle: String,
        challengeDescription: String,
        userMessage: String,
    ): String {
        return try {
            val conversationHistory =
                session.messages
                    .takeLast(10)
                    .joinToString("\n") { "${it.sender}: ${it.content}" }

            val prompt =
                promptTemplateService.createPromptFromTemplate(
                    "tutor-response",
                    mapOf(
                        "challengeTitle" to challengeTitle,
                        "challengeDescription" to challengeDescription,
                        "conversation" to conversationHistory,
                        "userMessage" to userMessage,
                    ),
                )

            val response = tutorChatClient.prompt(prompt).call()
            response.content() ?: "죄송합니다. 응답을 생성할 수 없습니다."
        } catch (e: Exception) {
            logger.error("Error generating tutor response", e)
            "죄송합니다. 일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요."
        }
    }

    @Retryable(
        value = [Exception::class],
        maxAttempts = 3,
        backoff = Backoff(delay = 1000, multiplier = 2.0),
    )
    private fun calculateUnderstandingScore(messages: List<Message>): Int {
        try {
            val conversation = messages.joinToString("\n") { "${it.sender}: ${it.content}" }

            val prompt =
                promptTemplateService.createPromptFromTemplate(
                    "understanding-score",
                    mapOf("conversation" to conversation),
                )

            val response = scoringChatClient.prompt(prompt).call()
            val scoreText = response.content()?.trim() ?: "70"

            return scoreText.filter { it.isDigit() }.toIntOrNull()?.coerceIn(0, 100) ?: 70
        } catch (e: Exception) {
            logger.warn("Error calculating understanding score, using default value", e)
            return 70
        }
    }

    private fun ChatSession.toResponseDto(): ChallengeChatOutput {
        return ChallengeChatOutput(
            sessionId = id ?: throw IllegalStateException("Session ID cannot be null"),
            challengeId = challengeId,
            userId = userId,
            messages =
                messages.map {
                    ChallengeChatOutput.ChatMessage(
                        content = it.content,
                        sender = it.sender,
                        timestamp = it.timestamp,
                    )
                },
            understandingScore = understandingScore,
            remainingInteractions = remainingInteractions,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )
    }
}
