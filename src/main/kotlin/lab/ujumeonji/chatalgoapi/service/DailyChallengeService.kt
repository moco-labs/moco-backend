package lab.ujumeonji.chatalgoapi.service

import lab.ujumeonji.chatalgoapi.model.Challenge
import lab.ujumeonji.chatalgoapi.model.DailyChallenge
import lab.ujumeonji.chatalgoapi.repository.ChallengeRepository
import lab.ujumeonji.chatalgoapi.repository.DailyChallengeRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDate
import kotlin.random.Random

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

    /**
     * 사용 가능한 모든 챌린지 중에서 랜덤으로 하나를 선택합니다.
     * 최근 사용된 일일 챌린지는 제외할 수 있습니다.
     *
     * @param excludeRecentDays 최근 n일 동안 사용된 챌린지를 제외 (기본값: 7일)
     * @return 선택된 챌린지 또는 사용 가능한 챌린지가 없으면 null
     */
    fun selectRandomChallenge(excludeRecentDays: Int = 7): Challenge? {
        // 모든 챌린지 가져오기
        val allChallenges = challengeRepository.findAll()
        if (allChallenges.isEmpty()) {
            logger.warn("사용 가능한 챌린지가 없습니다.")
            return null
        }

        // 최근 n일 동안 사용된 챌린지 ID 목록 가져오기
        val today = LocalDate.now()
        val startDate = today.minusDays(excludeRecentDays.toLong())
        val recentDailyChallenges = findByDateRange(startDate, today)
        val recentChallengeIds = recentDailyChallenges.map { it.challengeId }.toSet()

        // 최근에 사용되지 않은 챌린지 필터링
        val availableChallenges = allChallenges.filter { challenge ->
            challenge.id != null && challenge.id !in recentChallengeIds
        }

        if (availableChallenges.isEmpty()) {
            logger.warn("최근 {}일 동안 사용되지 않은 챌린지가 없습니다. 모든 챌린지에서 랜덤 선택합니다.", excludeRecentDays)
            // 모든 챌린지에서 랜덤 선택
            return allChallenges[Random.nextInt(allChallenges.size)]
        }

        // 사용 가능한 챌린지에서 랜덤 선택
        val randomIndex = Random.nextInt(availableChallenges.size)
        return availableChallenges[randomIndex]
    }

    /**
     * 랜덤 챌린지를 오늘의 일일 챌린지로 설정합니다.
     *
     * @param excludeRecentDays 최근 n일 동안 사용된 챌린지를 제외 (기본값: 7일)
     * @return 설정된 일일 챌린지 또는 설정 실패 시 null
     */
    fun setRandomDailyChallenge(excludeRecentDays: Int = 7): DailyChallenge? {
        val randomChallenge = selectRandomChallenge(excludeRecentDays)

        return if (randomChallenge?.id != null) {
            val today = LocalDate.now()
            setDailyChallenge(today, randomChallenge.id)
        } else {
            logger.error("랜덤 챌린지를 선택할 수 없습니다.")
            null
        }
    }
}
