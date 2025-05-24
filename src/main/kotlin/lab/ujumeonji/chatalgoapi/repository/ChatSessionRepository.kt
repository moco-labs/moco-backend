package lab.ujumeonji.chatalgoapi.repository

import lab.ujumeonji.chatalgoapi.model.ChatSession
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ChatSessionRepository : MongoRepository<ChatSession, String> {
    fun findByIdAndUserId(
        id: String,
        userId: String,
    ): ChatSession?

    fun findByChallengeIdAndUserId(
        challengeId: String,
        userId: String,
    ): ChatSession?

    fun findByUserId(userId: String): List<ChatSession>
}
