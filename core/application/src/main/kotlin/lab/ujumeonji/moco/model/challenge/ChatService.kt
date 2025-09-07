package lab.ujumeonji.moco.model.challenge

import lab.ujumeonji.moco.adapter.ChatSessionRepositoryAdapter
import lab.ujumeonji.moco.model.user.UserService
import lab.ujumeonji.moco.service.challenge.io.ChallengeChatInput
import lab.ujumeonji.moco.service.challenge.io.ChallengeChatOutput
import lab.ujumeonji.moco.support.error.BusinessException
import org.slf4j.LoggerFactory
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.messages.UserMessage
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ChatService(
    private val chatSessionRepositoryAdapter: ChatSessionRepositoryAdapter,
    private val challengeService: ChallengeService,
    private val chatClient: ChatClient,
    private val userService: UserService,
) {
    private val logger = LoggerFactory.getLogger(ChatService::class.java)
    private val understandingScoreCalculator =
        object : UnderstandingScoreCalculator {
            override fun calculateScore(messages: List<Message>): Int {
                val scoringPromptText =
                    """
                    Analyze the following conversation and evaluate the user's understanding of the problem on a scale of 0-100.

                    Evaluation Criteria:
                    - 80-100: Excellent understanding. The user demonstrates complete comprehension of the problem and clearly identifies the solution approach.
                    - 60-79: Good understanding. The user grasps most concepts but shows some gaps in understanding.
                    - 40-59: Basic understanding. The user understands fundamental concepts but struggles with solution direction.
                    - 20-39: Poor understanding. The user shows significant difficulty in comprehending the problem and needs substantial guidance.
                    - 0-19: Minimal understanding. The user shows almost no comprehension of the problem.

                    Consider these factors in your evaluation:
                    1. Clarity of questions asked
                    2. Relevance of responses to the problem context
                    3. Demonstrated grasp of key concepts
                    4. Logical progression in problem-solving approach

                    Conversation:
                    ${messages.joinToString("\n") { "${it.sender}: ${it.content}" }}

                    Respond with only a number between 0 and 100.
                    """.trimIndent()

                try {
                    val prompt = Prompt(listOf(UserMessage(scoringPromptText)))
                    val aiResponse = chatClient.prompt(prompt).call()
                    val scoreText = aiResponse.content()?.trim() ?: "0"

                    return scoreText.filter { it.isDigit() }.toIntOrNull()?.coerceIn(0, 100) ?: 70
                } catch (e: Exception) {
                    logger.error("Error calling Gemini API for scoring: ${e.message}", e)
                    return 70
                }
            }
        }

    fun processChat(
        challengeId: String,
        userId: String,
        request: ChallengeChatInput,
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

            val now = LocalDateTime.now()
            session.addUserMessage(request.message, now)

            val systemAnswer = getAnswer(session, challenge.title, challenge.description)
            session.addSystemMessage(systemAnswer, now)

            if (session.isLastInteraction) {
                session.understandingScore = understandingScoreCalculator.calculateScore(session.messages)
            }

            val savedSession = chatSessionRepositoryAdapter.save(session)
            return savedSession.toResponseDto()
        } catch (e: BusinessException) {
            throw e
        } catch (e: Exception) {
            logger.error("Error processing chat for challenge $challengeId and user $userId", e)
            throw BusinessException.chatProcessingFailed("채팅 처리 중 오류가 발생했습니다: ${e.message}")
        }
    }

    fun getChatSessionsByChallengeAndUser(
        challengeId: String,
        userId: String,
    ): ChallengeChatOutput {
        try {
            challengeService.findById(challengeId)
                ?: throw BusinessException.challengeNotFound(challengeId)

            val sessions = chatSessionRepositoryAdapter.findByChallengeIdAndUserId(challengeId, userId)

            return sessions?.toResponseDto() ?: ChallengeChatOutput(
                sessionId = "",
                challengeId = challengeId,
                userId = userId,
                messages = emptyList(),
                understandingScore = 0,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
            )
        } catch (e: BusinessException) {
            throw e
        } catch (e: Exception) {
            logger.error("Error getting chat sessions for challenge $challengeId and user $userId", e)
            throw BusinessException.chatProcessingFailed("채팅 세션 조회 중 오류가 발생했습니다: ${e.message}")
        }
    }

    private fun getAnswer(
        session: ChatSession,
        challengeTitle: String,
        challengeDescription: String,
    ): String {
        val systemPrompt =
            """
            You are a friendly AI tutor helping with algorithm problem solving.

            Current Problem: $challengeTitle
            Problem Description: $challengeDescription

            When responding to user questions about this problem, follow these guidelines:
            1. Don't provide easy answers. Give hints that allow the user to solve the problem independently.
            2. Provide specific help for areas where the user is stuck.
            3. Guide the user through a step-by-step approach.
            4. Ask questions to check understanding during the process.
            5. Offer encouragement and positive feedback.
            6. Respond in Korean language.

            Previous conversation:
            ${session.messages.joinToString("\n") { "${it.sender}: ${it.content}" }}
            """.trimIndent()

        try {
            val prompt = Prompt(listOf(UserMessage(systemPrompt)))
            val aiResponse = chatClient.prompt(prompt).call()
            return aiResponse.content() ?: "죄송합니다. 응답을 생성할 수 없습니다."
        } catch (e: Exception) {
            logger.error("Error calling Gemini API: ${e.message}", e)
            return "죄송합니다. 일시적인 오류가 발생했습니다. 다시 시도해주세요."
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
