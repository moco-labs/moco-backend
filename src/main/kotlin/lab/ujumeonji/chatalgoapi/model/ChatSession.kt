package lab.ujumeonji.chatalgoapi.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "chat_sessions")
@TypeAlias("ChatSession")
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
    val updatedAt: LocalDateTime = LocalDateTime.now(),
)

data class Message(
    val content: String,
    val sender: String,
    val timestamp: LocalDateTime = LocalDateTime.now(),
)
