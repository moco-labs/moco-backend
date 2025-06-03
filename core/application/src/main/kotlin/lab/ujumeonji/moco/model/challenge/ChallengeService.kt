package lab.ujumeonji.moco.model.challenge

import lab.ujumeonji.moco.adapter.ChallengeRepositoryAdapter
import lab.ujumeonji.moco.model.challenge.io.CreateChallengeInput
import lab.ujumeonji.moco.service.challenge.io.ChallengeOutput
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class ChallengeService(private val challengeRepositoryAdapter: ChallengeRepositoryAdapter) {
    private val logger = LoggerFactory.getLogger(ChallengeService::class.java)

    fun findAll(): List<Challenge> = challengeRepositoryAdapter.findAll()

    fun findAllOutput(): List<ChallengeOutput> = findAll().map { ChallengeOutput.fromDomain(it) }

    fun findAll(pageable: Pageable): Page<Challenge> = challengeRepositoryAdapter.findAll(pageable)

    fun findAllOutput(pageable: Pageable): Page<ChallengeOutput> {
        val challengePage = findAll(pageable)
        return PageImpl(
            challengePage.content.map { ChallengeOutput.fromDomain(it) },
            challengePage.pageable,
            challengePage.totalElements,
        )
    }

    fun findById(id: String): Challenge? = challengeRepositoryAdapter.findById(id)

    fun findByIdOutput(id: String): ChallengeOutput? = findById(id)?.let { ChallengeOutput.fromDomain(it) }

    fun findByTitle(title: String): Challenge? = challengeRepositoryAdapter.findByTitle(title)

    fun findByTitleOutput(title: String): ChallengeOutput? = findByTitle(title)?.let { ChallengeOutput.fromDomain(it) }

    fun findByDifficulty(
        difficulty: String,
        pageable: Pageable,
    ): Page<Challenge> = challengeRepositoryAdapter.findByDifficulty(difficulty, pageable)

    fun findByDifficultyOutput(
        difficulty: String,
        pageable: Pageable,
    ): Page<ChallengeOutput> {
        val challengePage = findByDifficulty(difficulty, pageable)
        return PageImpl(
            challengePage.content.map { ChallengeOutput.fromDomain(it) },
            challengePage.pageable,
            challengePage.totalElements,
        )
    }

    fun findByTag(
        tag: String,
        pageable: Pageable,
    ): Page<Challenge> = challengeRepositoryAdapter.findByTagsContaining(tag, pageable)

    fun findByTagOutput(
        tag: String,
        pageable: Pageable,
    ): Page<ChallengeOutput> {
        val challengePage = findByTag(tag, pageable)
        return PageImpl(
            challengePage.content.map { ChallengeOutput.fromDomain(it) },
            challengePage.pageable,
            challengePage.totalElements,
        )
    }

    fun save(request: CreateChallengeInput): Challenge {
        logger.info("저장 중인 챌린지: {}", request.title)
        return challengeRepositoryAdapter.save(request.toDomain())
    }

    fun saveOutput(request: CreateChallengeInput): ChallengeOutput {
        val challenge = save(request)
        return ChallengeOutput.fromDomain(challenge)
    }
}
