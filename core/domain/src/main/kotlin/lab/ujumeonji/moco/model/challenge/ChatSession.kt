package lab.ujumeonji.moco.model.challenge

import lab.ujumeonji.moco.model.user.User
import java.time.LocalDateTime
import java.util.UUID

interface UnderstandingScoreCalculator {
    fun calculateScore(messages: List<Message>): Int
}

class ChatSession(
    val id: String? = null,
    val challengeId: String,
    val userId: String,
    val messages: MutableList<Message> = mutableListOf(),
    var understandingScore: Int? = null,
    var interactionCount: Int = 0,
    val maxInteractions: Int = 5,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
) {
    val isLastInteraction: Boolean
        get() = interactionCount >= maxInteractions

    val remainingInteractions: Int
        get() = maxInteractions - interactionCount

    fun addUserMessage(content: String) {
        check(remainingInteractions > 0) {
            "Maximum interactions reached for this session"
        }

        interactionCount += 1

        messages.add(
            Message(
                content = content,
                sender = "user",
            ),
        )
    }

    fun addSystemMessage(content: String) {
        messages.add(
            Message(
                content = content,
                sender = "assistant",
            ),
        )
    }

    companion object {
        fun create(
            user: User,
            challenge: Challenge,
        ) = ChatSession(
            id = UUID.randomUUID().toString(),
            challengeId = challenge.id,
            userId = user.id,
        )
    }
}

class Message(
    val content: String,
    val sender: String,
    val timestamp: LocalDateTime = LocalDateTime.now(),
)
