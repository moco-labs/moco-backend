package lab.ujumeonji.moco.repository

import lab.ujumeonji.moco.model.ChallengeEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ChallengeRepository : MongoRepository<ChallengeEntity, String> {
    fun findByTitle(title: String): ChallengeEntity?

    fun findByDifficulty(difficulty: String): List<ChallengeEntity>

    fun findByDifficulty(
        difficulty: String,
        pageable: Pageable,
    ): Page<ChallengeEntity>

    fun findByTagsContaining(tag: String): List<ChallengeEntity>

    fun findByTagsContaining(
        tag: String,
        pageable: Pageable,
    ): Page<ChallengeEntity>

    override fun findAll(pageable: Pageable): Page<ChallengeEntity>
}
