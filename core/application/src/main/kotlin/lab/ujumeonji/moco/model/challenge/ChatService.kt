package lab.ujumeonji.moco.model.challenge

import lab.ujumeonji.moco.adapter.ChatSessionRepositoryAdapter
import lab.ujumeonji.moco.service.challenge.io.ChallengeChatInput
import lab.ujumeonji.moco.service.challenge.io.ChallengeChatOutput
import org.slf4j.LoggerFactory
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.messages.SystemMessage
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

@Service
class ChatService(
    private val chatSessionRepositoryAdapter: ChatSessionRepositoryAdapter,
    private val challengeService: ChallengeService,
    private val chatClient: ChatClient,
) {
    private val logger = LoggerFactory.getLogger(ChatService::class.java)

    fun processChat(
        challengeId: String,
        userId: String,
        request: ChallengeChatInput,
    ): ChallengeChatOutput {
        val challenge =
            challengeService.findById(challengeId)
                ?: throw IllegalArgumentException("Challenge not found with ID: $challengeId")

        val session =
            chatSessionRepositoryAdapter.findByChallengeIdAndUserId(challengeId, userId) ?: ChatSession(
                id = UUID.randomUUID().toString(),
                challengeId = challengeId,
                userId = userId,
            )

        if (session.isMaxInteractionsReached) {
            throw IllegalArgumentException("Maximum interactions reached for this session")
        }

        val userMessage =
            Message(
                content = request.message,
                sender = "user",
            )
        session.messages.add(userMessage)

        val systemResponse = generateResponse(session, challenge.title, challenge.description)
        session.messages.add(systemResponse)

        val updatedSession =
            ChatSession(
                id = session.id,
                challengeId = session.challengeId,
                userId = session.userId,
                messages = session.messages,
                understandingScore =
                    if (session.isLastInteraction) {
                        calculateUnderstandingScore(session)
                    } else {
                        session.understandingScore
                    },
                interactionCount = session.interactionCount + 1,
                maxInteractions = session.maxInteractions,
                createdAt = session.createdAt,
                updatedAt = LocalDateTime.now(),
            )

        val savedSession = chatSessionRepositoryAdapter.save(updatedSession)

        return savedSession.toResponseDto()
    }

    private fun generateResponse(
        session: ChatSession,
        challengeTitle: String,
        challengeDescription: String,
    ): Message {
        val systemPrompt =
            """
            당신은 알고리즘 문제 해결을 돕는 친절한 AI 튜터입니다.

            현재 문제: $challengeTitle
            문제 설명: $challengeDescription

            사용자가 이 문제에 대해 질문하면, 다음 지침을 따르세요:
            1. 너무 쉬운 답변을 주지 마세요. 사용자가 스스로 문제를 해결할 수 있도록 힌트만 제공하세요.
            2. 사용자가 막힌 부분에 대해 구체적으로 도움을 주세요.
            3. 단계별로 접근하도록 안내하세요.
            4. 중간 과정에서 사용자의 이해도를 확인하는 질문을 하세요.
            5. 격려와 긍정적인 피드백을 제공하세요.
            6. 답변은 한국어로 해주세요.

            이전 대화 내용:
            ${session.messages.joinToString("\n") { "${it.sender}: ${it.content}" }}
            """.trimIndent()

        try {
            val prompt = Prompt(listOf(SystemMessage(systemPrompt)))
            val aiResponse = chatClient.prompt(prompt).call()
            val responseContent = aiResponse.content() ?: "죄송합니다. 응답을 생성할 수 없습니다."

            return Message(
                content = responseContent,
                sender = "assistant",
            )
        } catch (e: Exception) {
            logger.error("Error calling Gemini API: ${e.message}", e)
            return Message(
                content = "죄송합니다. 일시적인 오류가 발생했습니다. 다시 시도해주세요.",
                sender = "assistant",
            )
        }
    }

    private fun calculateUnderstandingScore(session: ChatSession): Int {
        val scoringPromptText =
            """
            다음 대화 내용을 분석하여 사용자의 문제 이해도를 0-100 점수로 평가해주세요.
            평가 기준:
            - 80-100: 문제를 완전히 이해하고 해결 방향을 잘 파악함
            - 60-79: 문제를 대체로 이해하지만 일부 개념이 부족함
            - 40-59: 기본적인 이해는 있으나 해결 방향에 어려움이 있음
            - 20-39: 문제 이해가 부족하고 많은 도움이 필요함
            - 0-19: 문제를 거의 이해하지 못함

            대화 내용:
            ${session.messages.joinToString("\n") { "${it.sender}: ${it.content}" }}

            점수만 숫자로 응답해주세요.
            """.trimIndent()

        try {
            val prompt = Prompt(listOf(SystemMessage(scoringPromptText)))
            val aiResponse = chatClient.prompt(prompt).call()
            val scoreText = aiResponse.content()?.trim() ?: "0"

            return scoreText.filter { it.isDigit() }.toIntOrNull()?.coerceIn(0, 100) ?: 70
        } catch (e: Exception) {
            logger.error("Error calling Gemini API for scoring: ${e.message}", e)
            return 70
        }
    }

    private fun ChatSession.toResponseDto(): ChallengeChatOutput {
        return ChallengeChatOutput(
            sessionId = this.id ?: throw IllegalStateException("Session ID cannot be null"),
            challengeId = this.challengeId,
            userId = this.userId,
            messages =
                this.messages.map {
                    ChallengeChatOutput.ChatMessage(
                        content = it.content,
                        sender = it.sender,
                        timestamp = it.timestamp,
                    )
                },
            understandingScore = this.understandingScore,
            remainingInteractions = this.maxInteractions - this.interactionCount,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt,
        )
    }

    fun getChatSessionsByChallengeAndUser(
        challengeId: String,
        userId: String,
    ): ChallengeChatOutput {
        challengeService.findById(challengeId)
            ?: throw IllegalArgumentException("Challenge not found with ID: $challengeId")

        val sessions = chatSessionRepositoryAdapter.findByChallengeIdAndUserId(challengeId, userId)

        return sessions?.toResponseDto() ?: ChallengeChatOutput(
            sessionId = "",
            challengeId = challengeId,
            userId = userId,
            messages = emptyList(),
            understandingScore = 0,
            remainingInteractions = 5,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
        )
    }
}
