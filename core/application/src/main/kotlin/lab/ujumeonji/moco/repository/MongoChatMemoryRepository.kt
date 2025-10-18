package lab.ujumeonji.moco.repository

import lab.ujumeonji.moco.model.MessageEntity
import lab.ujumeonji.moco.model.challenge.MessageSender
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
            when (MessageSender.from(it.sender)) {
                MessageSender.ASSISTANT, MessageSender.SYSTEM ->
                    AssistantMessage(it.content, mapOf("timestamp" to it.timestamp))

                MessageSender.USER ->
                    UserMessage.builder()
                        .text(it.content)
                        .metadata(mapOf("timestamp" to it.timestamp))
                        .build()
            }
        }
    }

    override fun saveAll(
        conversationId: String,
        messages: List<Message>,
    ) {
        val mappedMessages =
            messages.map {
                MessageEntity(
                    it.text,
                    it.messageType.value,
                    it.metadata["timestamp"] as? LocalDateTime ?: LocalDateTime.now(),
                )
            }

        val session = chatSessionRepository.findByIdOrNull(conversationId) ?: return

        session.messages = mappedMessages.toMutableList()

        chatSessionRepository.save(session)
    }

    override fun deleteByConversationId(conversationId: String) = chatSessionRepository.deleteById(conversationId)
}
