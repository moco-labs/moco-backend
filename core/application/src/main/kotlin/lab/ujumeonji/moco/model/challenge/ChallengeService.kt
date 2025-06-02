package lab.ujumeonji.moco.model.challenge

import lab.ujumeonji.moco.adapter.ChallengeRepositoryAdapter
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ChallengeService(private val challengeRepositoryAdapter: ChallengeRepositoryAdapter) {
    private val logger = LoggerFactory.getLogger(ChallengeService::class.java)

    fun findAll(): List<Challenge> = challengeRepositoryAdapter.findAll()

    fun findAll(pageable: Pageable): Page<Challenge> = challengeRepositoryAdapter.findAll(pageable)

    fun findById(id: String): Challenge? = challengeRepositoryAdapter.findById(id)

    fun findByTitle(title: String): Challenge? = challengeRepositoryAdapter.findByTitle(title)

    fun findByDifficulty(difficulty: String): List<Challenge> = challengeRepositoryAdapter.findByDifficulty(difficulty)

    fun findByDifficulty(
        difficulty: String,
        pageable: Pageable,
    ): Page<Challenge> = challengeRepositoryAdapter.findByDifficulty(difficulty, pageable)

    fun findByTag(tag: String): List<Challenge> = challengeRepositoryAdapter.findByTagsContaining(tag)

    fun findByTag(
        tag: String,
        pageable: Pageable,
    ): Page<Challenge> = challengeRepositoryAdapter.findByTagsContaining(tag, pageable)

    fun save(challenge: Challenge): Challenge {
        logger.info("저장 중인 챌린지: {}", challenge.title)
        return challengeRepositoryAdapter.save(challenge)
    }

    fun update(
        id: String,
        updatedChallenge: Challenge,
    ): Challenge? {
        val existingChallenge = challengeRepositoryAdapter.findById(id)

        return if (existingChallenge != null) {
            val challenge =
                Challenge(
                    id = existingChallenge.id,
                    title = updatedChallenge.title,
                    description = updatedChallenge.description,
                    descriptionHighlightTokens = updatedChallenge.descriptionHighlightTokens,
                    instructions = updatedChallenge.instructions,
                    difficulty = updatedChallenge.difficulty,
                    tags = updatedChallenge.tags,
                    content = updatedChallenge.content,
                    examples = updatedChallenge.examples,
                    constraints = updatedChallenge.constraints,
                    createdAt = existingChallenge.createdAt,
                    updatedAt = LocalDateTime.now(),
                )
            challengeRepositoryAdapter.save(challenge)
        } else {
            null
        }
    }

    fun deleteById(id: String) {
        challengeRepositoryAdapter.deleteById(id)
    }
}
