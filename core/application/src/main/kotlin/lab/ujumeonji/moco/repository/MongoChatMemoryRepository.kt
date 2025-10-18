package lab.ujumeonji.moco.repository

import lab.ujumeonji.moco.model.MessageEntity
import org.springframework.ai.chat.memory.ChatMemoryRepository
import org.springframework.ai.chat.messages.AssistantMessage
import org.springframework.ai.chat.messages.Message
import org.springframework.ai.chat.messages.UserMessage
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class MongoChatMemoryRepository(
    private val chatSessionRepository: ChatSessionRepository,
) : ChatMemoryRepository {
    override fun findConversationIds(): List<String?> = chatSessionRepository.findAll().map { it.id }

    override fun findByConversationId(conversationId: String): List<Message?> {
        val session = chatSessionRepository.findByIdOrNull(conversationId) ?: return emptyList()

        return session.messages.map {
            when (it.sender) {
                "assistant" ->
                    AssistantMessage(
                        it.content,
                        mapOf("timestamp" to it.timestamp),
                    )

                else ->
                    UserMessage.builder().text(it.content)
                        .metadata(mapOf("timestamp" to it.timestamp))
                        .build()
            }
        }
    }

    override fun saveAll(
        conversationId: String,
        messages: List<Message>,
    ) {
        val messages =
            messages.map {
                MessageEntity(
                    it.text,
                    it.messageType.value,
                    it.metadata["timestamp"] as? LocalDateTime ?: LocalDateTime.now(),
                )
            }

        val session = chatSessionRepository.findByIdOrNull(conversationId) ?: return

        session.messages = messages.toMutableList()

        chatSessionRepository.save(session)
    }

    override fun deleteByConversationId(conversationId: String) = chatSessionRepository.deleteById(conversationId)
}
