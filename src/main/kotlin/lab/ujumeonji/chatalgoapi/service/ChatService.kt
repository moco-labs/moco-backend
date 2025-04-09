package lab.ujumeonji.chatalgoapi.service

import lab.ujumeonji.chatalgoapi.model.ChatMessage
import lab.ujumeonji.chatalgoapi.model.Conversation
import lab.ujumeonji.chatalgoapi.repository.ConversationRepository
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.messages.MessageType
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class ChatService(
    private val chatClient: ChatClient,
    private val conversationRepository: ConversationRepository
) {

    fun chat(userId: String, messageContent: String, conversationId: String? = null): Conversation {
        val conversation = conversationId?.let {
            conversationRepository.findById(it).orElse(null)
        } ?: Conversation(userId = userId)

        val userMessage = ChatMessage(type = MessageType.USER, content = messageContent)
        conversation.messages.add(userMessage)

        // 이전 대화 내용 포함하여 프롬프트 생성 (메모리 관리 필요)
        val chatMessages = conversation.messages.map {
            org.springframework.ai.chat.messages.ChatMessage(it.type, it.content)
        }

        val response = chatClient.prompt(Prompt(chatMessages))
            .call()
            .chatResponse

        val assistantMessage = ChatMessage(
            type = MessageType.ASSISTANT,
            content = response.result.output.content
        )
        conversation.messages.add(assistantMessage)
        conversation.updatedAt = Instant.now()

        return conversationRepository.save(conversation)
    }

    fun getConversationHistory(conversationId: String): Conversation? {
        return conversationRepository.findById(conversationId).orElse(null)
    }

    fun getUserConversations(userId: String): List<Conversation> {
        return conversationRepository.findByUserId(userId)
    }

    // 기존 getChatResponse 메소드는 chat 메소드로 대체되었으므로 제거하거나 주석 처리
    /*
    fun getChatResponse(message: String): String {
        return chatClient.prompt()
            .user(message)
            .call()
            .content()
    }
    */
} 