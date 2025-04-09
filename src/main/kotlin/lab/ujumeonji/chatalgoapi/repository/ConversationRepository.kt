package lab.ujumeonji.chatalgoapi.repository

import lab.ujumeonji.chatalgoapi.model.Conversation
import org.springframework.data.mongodb.repository.MongoRepository

interface ConversationRepository : MongoRepository<Conversation, String> {
    fun findByUserId(userId: String): List<Conversation>
    // 필요에 따라 추가적인 쿼리 메소드 정의 가능
} 