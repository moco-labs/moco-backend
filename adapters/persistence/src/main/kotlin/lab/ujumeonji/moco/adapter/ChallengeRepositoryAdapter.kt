package lab.ujumeonji.moco.adapter

import lab.ujumeonji.moco.model.Challenge
import lab.ujumeonji.moco.model.ChallengeMapper
import lab.ujumeonji.moco.repository.ChallengeRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import java.util.Optional

@Component
class ChallengeRepositoryAdapter(
    private val challengeRepository: ChallengeRepository,
    private val challengeMapper: ChallengeMapper,
) {
    fun findAll(): List<Challenge> {
        return challengeRepository.findAll().map { challengeMapper.toDomain(it) }
    }

    fun findAll(pageable: Pageable): Page<Challenge> {
        val entityPage = challengeRepository.findAll(pageable)
        val domainContent = entityPage.content.map { challengeMapper.toDomain(it) }
        return PageImpl(domainContent, pageable, entityPage.totalElements)
    }

    fun findById(id: String): Optional<Challenge> {
        return challengeRepository.findById(id).map { challengeMapper.toDomain(it) }
    }

    fun findByTitle(title: String): Challenge? {
        return challengeRepository.findByTitle(title)?.let { challengeMapper.toDomain(it) }
    }

    fun findByDifficulty(difficulty: String): List<Challenge> {
        return challengeRepository.findByDifficulty(difficulty).map { challengeMapper.toDomain(it) }
    }

    fun findByDifficulty(
        difficulty: String,
        pageable: Pageable,
    ): Page<Challenge> {
        val entityPage = challengeRepository.findByDifficulty(difficulty, pageable)
        val domainContent = entityPage.content.map { challengeMapper.toDomain(it) }
        return PageImpl(domainContent, pageable, entityPage.totalElements)
    }

    fun findByTagsContaining(tag: String): List<Challenge> {
        return challengeRepository.findByTagsContaining(tag).map { challengeMapper.toDomain(it) }
    }

    fun findByTagsContaining(
        tag: String,
        pageable: Pageable,
    ): Page<Challenge> {
        val entityPage = challengeRepository.findByTagsContaining(tag, pageable)
        val domainContent = entityPage.content.map { challengeMapper.toDomain(it) }
        return PageImpl(domainContent, pageable, entityPage.totalElements)
    }

    fun save(challenge: Challenge): Challenge {
        val entity = challengeMapper.toEntity(challenge)
        val savedEntity = challengeRepository.save(entity)
        return challengeMapper.toDomain(savedEntity)
    }

    fun deleteById(id: String) {
        challengeRepository.deleteById(id)
    }
}
