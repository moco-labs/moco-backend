package lab.ujumeonji.chatalgoapi.dto

import java.time.LocalDateTime

data class ChallengeChatResponse(
    val sessionId: String,
    val challengeId: String,
    val userId: String,
    val messages: List<ChatMessage>,
    val understandingScore: Int? = null,
    val remainingInteractions: Int = 5,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
) {
    data class ChatMessage(
        val content: String,
        val sender: String,
        val timestamp: LocalDateTime = LocalDateTime.now(),
    )
}
