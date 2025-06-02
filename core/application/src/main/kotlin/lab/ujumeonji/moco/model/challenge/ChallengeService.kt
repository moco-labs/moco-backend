package lab.ujumeonji.moco.model.challenge

import lab.ujumeonji.moco.adapter.ChallengeRepositoryAdapter
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class ChallengeService(private val challengeRepositoryAdapter: ChallengeRepositoryAdapter) {
    private val logger = LoggerFactory.getLogger(ChallengeService::class.java)

    fun findAll(): List<Challenge> = challengeRepositoryAdapter.findAll()

    fun findAll(pageable: Pageable): Page<Challenge> = challengeRepositoryAdapter.findAll(pageable)

    fun findById(id: String): Challenge? = challengeRepositoryAdapter.findById(id)

    fun findByTitle(title: String): Challenge? = challengeRepositoryAdapter.findByTitle(title)

    fun findByDifficulty(
        difficulty: String,
        pageable: Pageable,
    ): Page<Challenge> = challengeRepositoryAdapter.findByDifficulty(difficulty, pageable)

    fun findByTag(
        tag: String,
        pageable: Pageable,
    ): Page<Challenge> = challengeRepositoryAdapter.findByTagsContaining(tag, pageable)

    fun save(challenge: Challenge): Challenge {
        logger.info("저장 중인 챌린지: {}", challenge.title)
        return challengeRepositoryAdapter.save(challenge)
    }
}
