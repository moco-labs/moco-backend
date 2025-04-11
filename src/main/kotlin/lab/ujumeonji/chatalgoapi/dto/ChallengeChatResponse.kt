package lab.ujumeonji.chatalgoapi.dto

import java.time.LocalDateTime

/**
 * DTO for challenge chat responses.
 * 
 * @property sessionId Unique identifier for the chat session
 * @property challengeId ID of the challenge being discussed
 * @property userId ID of the user in the conversation
 * @property messages List of messages in the conversation
 * @property understandingScore Optional score indicating user's understanding (0-100)
 * @property remainingInteractions Number of interactions remaining in this session
 * @property createdAt When the session was created
 * @property updatedAt When the session was last updated
 */
data class ChallengeChatResponse(
    val sessionId: String,
    val challengeId: String,
    val userId: String,
    val messages: List<ChatMessage>,
    val understandingScore: Int? = null,
    val remainingInteractions: Int = 5,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)

/**
 * Represents a single message in a chat conversation.
 * 
 * @property content The text content of the message
 * @property sender The sender of the message ("user" or "system")
 * @property timestamp When the message was sent
 */
data class ChatMessage(
    val content: String,
    val sender: String, // "user" or "system"
    val timestamp: LocalDateTime = LocalDateTime.now()
)
