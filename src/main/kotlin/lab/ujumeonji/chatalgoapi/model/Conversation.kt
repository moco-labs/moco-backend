package lab.ujumeonji.chatalgoapi.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = "conversations")
data class Conversation(
    @Id
    val id: String? = null,
    val userId: String, // 사용자 식별자 (필요에 따라 추가/수정)
    val messages: MutableList<ChatMessage> = mutableListOf(),
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now()
) 