package lab.ujumeonji.chatalgoapi.service

import lab.ujumeonji.chatalgoapi.dto.ChallengeChatRequest
import lab.ujumeonji.chatalgoapi.dto.ChallengeChatResponse
import lab.ujumeonji.chatalgoapi.dto.ChatMessage
import lab.ujumeonji.chatalgoapi.model.ChatSession
import lab.ujumeonji.chatalgoapi.model.Message
import lab.ujumeonji.chatalgoapi.repository.ChatSessionRepository
import org.slf4j.LoggerFactory
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.messages.AssistantMessage
import org.springframework.ai.chat.messages.SystemMessage
import org.springframework.ai.chat.messages.UserMessage
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*
import org.springframework.ai.chat.messages.Message as AiMessage

@Service
class ChatService(
    private val chatSessionRepository: ChatSessionRepository,
    private val challengeService: ChallengeService,
    private val chatClient: ChatClient
) {
    private val logger = LoggerFactory.getLogger(ChatService::class.java)

    /**
     * Process a chat message for a specific challenge.
     * If a sessionId is provided, it continues that session; otherwise, it creates a new one.
     *
     * @param challengeId The ID of the challenge being discussed
     * @param request The chat request containing user ID, message, and optional session ID
     * @return The updated chat session information
     */
    fun processChat(challengeId: String, request: ChallengeChatRequest): ChallengeChatResponse {
        // Verify the challenge exists
        val challenge = challengeService.findById(challengeId)
            ?: throw IllegalArgumentException("Challenge not found with ID: $challengeId")

        // Get or create session
        val session = if (request.sessionId != null) {
            // Find existing session
            val existingSession = chatSessionRepository.findByIdAndUserId(request.sessionId, request.userId)
                ?: throw IllegalArgumentException("Session not found or not owned by user")

            // Check if session is for the correct challenge
            if (existingSession.challengeId != challengeId) {
                throw IllegalArgumentException("Session is for a different challenge")
            }

            // Check if max interactions reached
            if (existingSession.interactionCount >= existingSession.maxInteractions) {
                throw IllegalArgumentException("Maximum interactions reached for this session")
            }

            existingSession
        } else {
            // Create new session
            ChatSession(
                id = UUID.randomUUID().toString(),
                challengeId = challengeId,
                userId = request.userId
            )
        }

        // Add user message to session
        val userMessage = Message(
            content = request.message,
            sender = "user"
        )
        session.messages.add(userMessage)

        // Generate system response
        val systemResponse = generateResponse(session, challenge.title, challenge.description)
        session.messages.add(systemResponse)

        // Update session metadata
        val updatedSession = session.copy(
            interactionCount = session.interactionCount + 1,
            updatedAt = LocalDateTime.now(),
            // If this is the final interaction, calculate understanding score
            understandingScore = if (session.interactionCount + 1 >= session.maxInteractions) {
                calculateUnderstandingScore(session)
            } else {
                session.understandingScore
            }
        )

        // Save updated session
        val savedSession = chatSessionRepository.save(updatedSession)

        // Convert to response DTO
        return savedSession.toResponseDto()
    }

    /**
     * Generate a response to the user's message using Gemini via Spring AI.
     */
    private fun generateResponse(session: ChatSession, challengeTitle: String, challengeDescription: String): Message {
        // Create a system prompt that establishes context for the AI
        val systemPromptText = """
            당신은 알고리즘 문제 해결을 돕는 코딩 튜터입니다.
            
            아래는 사용자가 풀고 있는 알고리즘 챌린지의 정보입니다:
            제목: $challengeTitle
            설명: $challengeDescription
            
            사용자가 이 문제를 이해하고 해결하도록 도와주세요.
            직접적인 답을 제공하지 마세요. 대신 생각하는 방향과 접근법을 안내해 주세요.
            알고리즘 개념과 자료구조에 대해 설명해 주세요.
            문제 해결 전략과 시간/공간 복잡도를 설명해 주세요.
            친절하고 도움이 되는 방식으로 답변하되, 한국어로 대화해 주세요.
        """.trimIndent()

        // Prepare conversation history for context
        val messages = mutableListOf<AiMessage>()

        // Add system message first
        messages.add(SystemMessage(systemPromptText))

        // Add conversation history (up to last 5 messages for context)
        val conversationHistory = session.messages.takeLast(5)
        conversationHistory.forEach { message ->
            if (message.sender == "user") {
                messages.add(UserMessage(message.content))
            } else {
                messages.add(AssistantMessage(message.content))
            }
        }

        // If this is a completely new session with no messages yet
        if (session.messages.isEmpty()) {
            messages.add(UserMessage("이 알고리즘 문제에 대해 설명해 주세요."))
        }

        // Create prompt and get response from Gemini
        val prompt = Prompt(messages)

        try {
            val aiResponse = chatClient.prompt(prompt).call()
            val responseContent = aiResponse.content()?.trim() ?: ""

            return Message(
                content = responseContent,
                sender = "system"
            )
        } catch (e: Exception) {
            logger.error("Error calling Gemini API: ${e.message}", e)
            // Fallback response in case of API failure
            return Message(
                content = "죄송합니다. 현재 응답을 생성하는 중에 문제가 발생했습니다. 잠시 후 다시 시도해 주세요.",
                sender = "system"
            )
        }
    }

    /**
     * Calculate an understanding score based on the conversation using AI.
     */
    private fun calculateUnderstandingScore(session: ChatSession): Int {
        val challenge = challengeService.findById(session.challengeId)
            ?: return 70 // Default score if challenge not found

        // Create a prompt specifically for scoring understanding
        val scoringPromptText = """
            당신은 알고리즘 이해도를 평가하는 교육 전문가입니다.
            
            아래는 사용자가 풀고 있는 알고리즘 챌린지의 정보입니다:
            제목: ${challenge.title}
            설명: ${challenge.description}
            
            아래는 사용자와의 대화 내용입니다:
            ${
            session.messages.joinToString("\n") {
                val role = if (it.sender == "user") "사용자" else "시스템"
                "$role: ${it.content}"
            }
        }
            
            대화를 기반으로 사용자의 알고리즘 문제 이해도를 0-100 점수로 평가해주세요.
            다음 기준으로 평가하세요:
            - 문제를 정확히 이해했는가?
            - 적절한 알고리즘 개념을 언급했는가?
            - 효율적인 해결 방법을 고려했는가?
            - 시간/공간 복잡도를 이해했는가?
            
            점수만 숫자로 반환해주세요. 다른 설명은 불필요합니다.
        """.trimIndent()

        try {
            // Create prompt and get score from Gemini
            val prompt = Prompt(listOf(SystemMessage(scoringPromptText)))
            val aiResponse = chatClient.prompt(prompt).call()
            val scoreText = aiResponse.content()?.trim() ?: "0"

            // Extract the score as a number
            return scoreText.filter { it.isDigit() }.toIntOrNull()?.coerceIn(0, 100) ?: 70
        } catch (e: Exception) {
            logger.error("Error calling Gemini API for scoring: ${e.message}", e)
            return 70 // Default fallback score
        }
    }

    /**
     * Convert a ChatSession model to a ChallengeChatResponse DTO.
     */
    private fun ChatSession.toResponseDto(): ChallengeChatResponse {
        return ChallengeChatResponse(
            sessionId = this.id ?: throw IllegalStateException("Session ID cannot be null"),
            challengeId = this.challengeId,
            userId = this.userId,
            messages = this.messages.map {
                ChatMessage(
                    content = it.content,
                    sender = it.sender,
                    timestamp = it.timestamp
                )
            },
            understandingScore = this.understandingScore,
            remainingInteractions = this.maxInteractions - this.interactionCount,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt
        )
    }
}
