package lab.ujumeonji.moco.service.challenge.io

import lab.ujumeonji.moco.model.challenge.MessageSender
import java.time.LocalDateTime

data class ChallengeChatOutput(
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
        val sender: MessageSender,
        val timestamp: LocalDateTime = LocalDateTime.now(),
    )

    companion object {
        fun emptyOutput(
            challengeId: String,
            userId: String,
            now: LocalDateTime = LocalDateTime.now(),
        ): ChallengeChatOutput =
            ChallengeChatOutput(
                sessionId = "",
                challengeId = challengeId,
                userId = userId,
                messages = emptyList(),
                understandingScore = 0,
                createdAt = now,
                updatedAt = now,
            )
    }
}
