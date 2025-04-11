package lab.ujumeonji.chatalgoapi.repository

import lab.ujumeonji.chatalgoapi.model.ChatSession
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ChatSessionRepository : MongoRepository<ChatSession, String> {
    /**
     * Find a chat session by its ID and user ID.
     */
    fun findByIdAndUserId(id: String, userId: String): ChatSession?

    /**
     * Find chat sessions by challenge ID and user ID.
     */
    fun findByChallengeIdAndUserId(challengeId: String, userId: String): List<ChatSession>

    /**
     * Find chat sessions by user ID.
     */
    fun findByUserId(userId: String): List<ChatSession>
}
