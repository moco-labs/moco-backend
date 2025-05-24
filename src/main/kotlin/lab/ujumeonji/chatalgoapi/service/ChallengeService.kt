package lab.ujumeonji.chatalgoapi.service

import lab.ujumeonji.chatalgoapi.model.Challenge
import lab.ujumeonji.chatalgoapi.repository.ChallengeRepository
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ChallengeService(private val challengeRepository: ChallengeRepository) {
    private val logger = LoggerFactory.getLogger(ChallengeService::class.java)

    fun findAll(): List<Challenge> = challengeRepository.findAll()

    fun findAll(pageable: Pageable): Page<Challenge> = challengeRepository.findAll(pageable)

    fun findById(id: String): Challenge? = challengeRepository.findById(id).orElse(null)

    fun findByTitle(title: String): Challenge? = challengeRepository.findByTitle(title)

    fun findByDifficulty(difficulty: String): List<Challenge> = challengeRepository.findByDifficulty(difficulty)

    fun findByDifficulty(
        difficulty: String,
        pageable: Pageable,
    ): Page<Challenge> = challengeRepository.findByDifficulty(difficulty, pageable)

    fun findByTag(tag: String): List<Challenge> = challengeRepository.findByTagsContaining(tag)

    fun findByTag(
        tag: String,
        pageable: Pageable,
    ): Page<Challenge> = challengeRepository.findByTagsContaining(tag, pageable)

    fun save(challenge: Challenge): Challenge {
        logger.info("저장 중인 챌린지: {}", challenge.title)
        return challengeRepository.save(challenge)
    }

    fun update(
        id: String,
        updatedChallenge: Challenge,
    ): Challenge? {
        val existingChallenge = challengeRepository.findById(id).orElse(null)

        return if (existingChallenge != null) {
            val challenge =
                updatedChallenge.copy(
                    id = existingChallenge.id,
                    createdAt = existingChallenge.createdAt,
                    updatedAt = LocalDateTime.now(),
                )
            challengeRepository.save(challenge)
        } else {
            null
        }
    }

    fun deleteById(id: String) {
        challengeRepository.deleteById(id)
    }
}
