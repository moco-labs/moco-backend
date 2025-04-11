package lab.ujumeonji.chatalgoapi.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

/**
 * Represents a chat session for a challenge.
 * Stores the conversation history and related metadata.
 */
@Document(collection = "chat_sessions")
data class ChatSession(
    @Id
    val id: String? = null,
    val challengeId: String,
    val userId: String,
    val messages: MutableList<Message> = mutableListOf(),
    val understandingScore: Int? = null,
    val interactionCount: Int = 0,
    val maxInteractions: Int = 5,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)

/**
 * Represents a single message in the chat session.
 */
data class Message(
    val content: String,
    val sender: String, // "user" or "system"
    val timestamp: LocalDateTime = LocalDateTime.now()
)
