package lab.ujumeonji.moco.model.challenge

import lab.ujumeonji.moco.model.user.User
import java.time.LocalDateTime
import java.util.UUID

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

    private var cachedConversationId: String? = null

    val conversationId: String
        get() = id ?: cachedConversationId ?: UUID.randomUUID().toString().also { cachedConversationId = it }

    fun addUserMessage(
        content: String,
        now: LocalDateTime = LocalDateTime.now(),
    ) {
        check(remainingInteractions > 0) {
            "Maximum interactions reached for this session"
        }

        interactionCount += 1

        messages.add(
            Message(
                content = content,
                sender = MessageSender.USER,
                timestamp = now,
            ),
        )
    }

    fun addSystemMessage(
        content: String,
        now: LocalDateTime = LocalDateTime.now(),
    ) {
        messages.add(
            Message(
                content = content,
                sender = MessageSender.ASSISTANT,
                timestamp = now,
            ),
        )
    }

    companion object {
        fun create(
            user: User,
            challengeId: String,
        ) = ChatSession(
            id = UUID.randomUUID().toString(),
            challengeId = challengeId,
            userId = user.id,
        )
    }
}

data class Message(
    val content: String,
    val sender: MessageSender,
    val timestamp: LocalDateTime = LocalDateTime.now(),
)

enum class MessageSender(val value: String) {
    USER("user"),
    SYSTEM("system"),
    ASSISTANT("assistant"),
    ;

    companion object {
        fun from(value: String): MessageSender {
            return entries.firstOrNull { it.value.equals(value, ignoreCase = true) }
                ?: throw IllegalArgumentException("Unknown message sender: $value")
        }
    }
}
