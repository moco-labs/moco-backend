package lab.ujumeonji.chatalgoapi.repository

import lab.ujumeonji.chatalgoapi.model.Challenge
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ChallengeRepository : MongoRepository<Challenge, String> {
    fun findByTitle(title: String): Challenge?

    fun findByDifficulty(difficulty: String): List<Challenge>

    fun findByDifficulty(
        difficulty: String,
        pageable: Pageable,
    ): Page<Challenge>

    fun findByTagsContaining(tag: String): List<Challenge>

    fun findByTagsContaining(
        tag: String,
        pageable: Pageable,
    ): Page<Challenge>

    override fun findAll(pageable: Pageable): Page<Challenge>
}
