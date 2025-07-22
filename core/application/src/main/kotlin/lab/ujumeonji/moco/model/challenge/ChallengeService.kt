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

    fun findAll(pageable: Pageable): Page<ChallengeOutput> {
        val challengePage = challengeRepositoryAdapter.findAll(pageable)
        return PageImpl(
            challengePage.content.map { ChallengeOutput.fromDomain(it) },
            challengePage.pageable,
            challengePage.totalElements,
        )
    }

    fun findById(id: String): ChallengeOutput? = challengeRepositoryAdapter.findById(id)?.let { ChallengeOutput.fromDomain(it) }

    fun findByTitle(title: String): ChallengeOutput? = challengeRepositoryAdapter.findByTitle(title)?.let { ChallengeOutput.fromDomain(it) }

    fun findByDifficulty(
        difficulty: String,
        pageable: Pageable,
    ): Page<ChallengeOutput> {
        val challengePage = challengeRepositoryAdapter.findByDifficulty(difficulty, pageable)
        return PageImpl(
            challengePage.content.map { ChallengeOutput.fromDomain(it) },
            challengePage.pageable,
            challengePage.totalElements,
        )
    }

    fun findByTag(
        tag: String,
        pageable: Pageable,
    ): Page<ChallengeOutput> {
        val challengePage = challengeRepositoryAdapter.findByTagsContaining(tag, pageable)
        return PageImpl(
            challengePage.content.map { ChallengeOutput.fromDomain(it) },
            challengePage.pageable,
            challengePage.totalElements,
        )
    }

    fun create(request: CreateChallengeInput): ChallengeOutput {
        logger.info("저장 중인 챌린지: {}", request.title)
        return challengeRepositoryAdapter.save(request.toDomain()).let(ChallengeOutput::fromDomain)
    }
}
