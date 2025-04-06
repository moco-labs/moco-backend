package lab.ujumeonji.chatalgoapi.service

import lab.ujumeonji.chatalgoapi.model.Challenge
import lab.ujumeonji.chatalgoapi.repository.ChallengeRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ChallengeService(private val challengeRepository: ChallengeRepository) {

    private val logger = LoggerFactory.getLogger(ChallengeService::class.java)

    /**
     * 모든 챌린지를 조회합니다.
     */
    fun findAll(): List<Challenge> = challengeRepository.findAll()

    /**
     * ID로 특정 챌린지를 조회합니다.
     */
    fun findById(id: String): Challenge? = challengeRepository.findById(id).orElse(null)

    /**
     * 제목으로 특정 챌린지를 조회합니다.
     */
    fun findByTitle(title: String): Challenge? = challengeRepository.findByTitle(title)

    /**
     * 난이도별로 챌린지를 조회합니다.
     */
    fun findByDifficulty(difficulty: String): List<Challenge> =
        challengeRepository.findByDifficulty(difficulty)

    /**
     * 태그별로 챌린지를 조회합니다.
     */
    fun findByTag(tag: String): List<Challenge> =
        challengeRepository.findByTagsContaining(tag)

    /**
     * 챌린지를 저장합니다.
     */
    fun save(challenge: Challenge): Challenge {
        logger.info("저장 중인 챌린지: {}", challenge.title)
        return challengeRepository.save(challenge)
    }

    /**
     * 기존 챌린지를 업데이트합니다.
     */
    fun update(id: String, updatedChallenge: Challenge): Challenge? {
        val existingChallenge = challengeRepository.findById(id).orElse(null)

        return if (existingChallenge != null) {
            val challenge = updatedChallenge.copy(
                id = existingChallenge.id,
                createdAt = existingChallenge.createdAt,
                updatedAt = LocalDateTime.now()
            )
            challengeRepository.save(challenge)
        } else {
            null
        }
    }

    /**
     * 챌린지를 ID로 삭제합니다.
     */
    fun deleteById(id: String) {
        challengeRepository.deleteById(id)
    }
}
