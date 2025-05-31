package lab.ujumeonji.moco.model

import java.time.LocalDateTime

class ChatSession(
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

class Message(
    val content: String,
    val sender: String,
    val timestamp: LocalDateTime = LocalDateTime.now(),
)
