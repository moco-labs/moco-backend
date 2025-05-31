package lab.ujumeonji.moco.repository

import lab.ujumeonji.moco.model.ChatSessionEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ChatSessionRepository : MongoRepository<ChatSessionEntity, String> {
    fun findByIdAndUserId(
        id: String,
        userId: String,
    ): ChatSessionEntity?

    fun findByChallengeIdAndUserId(
        challengeId: String,
        userId: String,
    ): ChatSessionEntity?

    fun findByUserId(userId: String): List<ChatSessionEntity>
}
