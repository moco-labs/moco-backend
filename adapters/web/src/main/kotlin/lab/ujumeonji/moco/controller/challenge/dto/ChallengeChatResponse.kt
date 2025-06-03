package lab.ujumeonji.moco.controller.challenge.dto

import lab.ujumeonji.moco.service.challenge.io.ChallengeChatOutput
import java.time.LocalDateTime

data class ChallengeChatResponse(
    val sessionId: String,
    val challengeId: String,
    val userId: String,
    val messages: List<ChatMessageResponse>,
    val understandingScore: Int? = null,
    val remainingInteractions: Int = 5,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {
    data class ChatMessageResponse(
        val content: String,
        val sender: String,
        val timestamp: LocalDateTime,
    )

    companion object {
        fun from(output: ChallengeChatOutput): ChallengeChatResponse {
            return ChallengeChatResponse(
                sessionId = output.sessionId,
                challengeId = output.challengeId,
                userId = output.userId,
                messages =
                    output.messages.map {
                        ChatMessageResponse(
                            content = it.content,
                            sender = it.sender,
                            timestamp = it.timestamp,
                        )
                    },
                understandingScore = output.understandingScore,
                remainingInteractions = output.remainingInteractions,
                createdAt = output.createdAt,
                updatedAt = output.updatedAt,
            )
        }
    }
}
