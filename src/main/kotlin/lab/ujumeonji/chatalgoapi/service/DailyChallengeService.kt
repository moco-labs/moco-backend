package lab.ujumeonji.chatalgoapi.service

import lab.ujumeonji.chatalgoapi.model.Challenge
import lab.ujumeonji.chatalgoapi.model.DailyChallenge
import lab.ujumeonji.chatalgoapi.repository.ChallengeRepository
import lab.ujumeonji.chatalgoapi.repository.DailyChallengeRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class DailyChallengeService(
    private val dailyChallengeRepository: DailyChallengeRepository,
    private val challengeRepository: ChallengeRepository
) {
    private val logger = LoggerFactory.getLogger(DailyChallengeService::class.java)

    /**
     * 모든 일일 챌린지를 조회합니다.
     */
    fun findAll(): List<DailyChallenge> = dailyChallengeRepository.findAll()

    /**
     * ID로 특정 일일 챌린지를 조회합니다.
     */
    fun findById(id: String): DailyChallenge? = dailyChallengeRepository.findById(id).orElse(null)

    /**
     * 오늘의 챌린지를 조회합니다.
     */
    fun findToday(): DailyChallenge? = dailyChallengeRepository.findByDate(LocalDate.now())

    /**
     * 특정 날짜의 일일 챌린지를 조회합니다.
     */
    fun findByDate(date: LocalDate): DailyChallenge? = dailyChallengeRepository.findByDate(date)

    /**
     * 특정 날짜 범위 내의 모든 일일 챌린지를 조회합니다.
     */
    fun findByDateRange(startDate: LocalDate, endDate: LocalDate): List<DailyChallenge> =
        dailyChallengeRepository.findByDateBetween(startDate, endDate)

    /**
     * 특정 날짜의 일일 챌린지에 해당하는 실제 챌린지 정보를 조회합니다.
     */
    fun findChallengeForDate(date: LocalDate): Challenge? {
        val dailyChallenge = dailyChallengeRepository.findByDate(date)
        return if (dailyChallenge != null) {
            challengeRepository.findById(dailyChallenge.challengeId).orElse(null)
        } else {
            null
        }
    }

    /**
     * 오늘의 챌린지에 해당하는 실제 챌린지 정보를 조회합니다.
     */
    fun findTodayChallenge(): Challenge? {
        val today = LocalDate.now()
        return findChallengeForDate(today)
    }

    /**
     * 일일 챌린지를 저장합니다.
     */
    fun save(dailyChallenge: DailyChallenge): DailyChallenge {
        // 챌린지가 존재하는지 확인
        val challenge = challengeRepository.findById(dailyChallenge.challengeId)
        if (challenge.isEmpty) {
            throw IllegalArgumentException("Challenge with ID ${dailyChallenge.challengeId} does not exist")
        }

        // 같은 날짜에 이미 일일 챌린지가 존재하는지 확인
        val existingDailyChallenge = dailyChallengeRepository.findByDate(dailyChallenge.date)
        if (existingDailyChallenge != null && existingDailyChallenge.id != dailyChallenge.id) {
            // 기존 일일 챌린지를 비활성화
            val updatedExisting = existingDailyChallenge.copy(isActive = false)
            dailyChallengeRepository.save(updatedExisting)
            logger.info("기존 일일 챌린지 비활성화: id = {}, date = {}", existingDailyChallenge.id, existingDailyChallenge.date)
        }

        logger.info("일일 챌린지 저장 중: 챌린지 ID = {}, 날짜 = {}", dailyChallenge.challengeId, dailyChallenge.date)
        return dailyChallengeRepository.save(dailyChallenge)
    }

    /**
     * 일일 챌린지를 업데이트합니다.
     */
    fun update(id: String, updatedDailyChallenge: DailyChallenge): DailyChallenge? {
        val existingDailyChallenge = dailyChallengeRepository.findById(id).orElse(null)

        return if (existingDailyChallenge != null) {
            // 챌린지가 존재하는지 확인
            val challenge = challengeRepository.findById(updatedDailyChallenge.challengeId)
            if (challenge.isEmpty) {
                throw IllegalArgumentException("Challenge with ID ${updatedDailyChallenge.challengeId} does not exist")
            }

            val dailyChallenge = updatedDailyChallenge.copy(
                id = existingDailyChallenge.id
            )
            dailyChallengeRepository.save(dailyChallenge)
        } else {
            null
        }
    }

    /**
     * 일일 챌린지를 삭제합니다.
     */
    fun deleteById(id: String) {
        dailyChallengeRepository.deleteById(id)
    }

    /**
     * 특정 날짜의 일일 챌린지를 설정합니다.
     * 이미 해당 날짜에 일일 챌린지가 존재하면 업데이트합니다.
     */
    fun setDailyChallenge(date: LocalDate, challengeId: String): DailyChallenge {
        // 챌린지가 존재하는지 확인
        val challenge = challengeRepository.findById(challengeId)
        if (challenge.isEmpty) {
            throw IllegalArgumentException("Challenge with ID $challengeId does not exist")
        }

        // 해당 날짜에 일일 챌린지가 이미 존재하는지 확인
        val existingDailyChallenge = dailyChallengeRepository.findByDate(date)

        return if (existingDailyChallenge != null) {
            // 기존 일일 챌린지 업데이트
            val updatedDailyChallenge = existingDailyChallenge.copy(
                challengeId = challengeId,
                isActive = true
            )
            logger.info("기존 일일 챌린지 업데이트: id = {}, date = {}", existingDailyChallenge.id, date)
            dailyChallengeRepository.save(updatedDailyChallenge)
        } else {
            // 새 일일 챌린지 생성
            val newDailyChallenge = DailyChallenge(
                challengeId = challengeId,
                date = date,
                isActive = true
            )
            logger.info("새 일일 챌린지 생성: 날짜 = {}", date)
            dailyChallengeRepository.save(newDailyChallenge)
        }
    }
}
