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
    private val challengeRepository: ChallengeRepository,
) {
    private val logger = LoggerFactory.getLogger(DailyChallengeService::class.java)

    fun findAll(): List<DailyChallenge> = dailyChallengeRepository.findAll()

    fun findById(id: String): DailyChallenge? = dailyChallengeRepository.findById(id).orElse(null)

    fun findToday(): DailyChallenge? = dailyChallengeRepository.findByDate(LocalDate.now())

    fun findByDate(date: LocalDate): DailyChallenge? = dailyChallengeRepository.findByDate(date)

    fun findByDateRange(
        startDate: LocalDate,
        endDate: LocalDate,
    ): List<DailyChallenge> = dailyChallengeRepository.findByDateBetween(startDate, endDate)

    fun findChallengeForDate(date: LocalDate): Challenge? {
        val dailyChallenge = dailyChallengeRepository.findByDate(date)
        return if (dailyChallenge != null) {
            challengeRepository.findById(dailyChallenge.challengeId).orElse(null)
        } else {
            null
        }
    }

    fun findTodayChallenge(): Challenge? {
        val today = LocalDate.now()
        return findChallengeForDate(today)
    }

    fun save(dailyChallenge: DailyChallenge): DailyChallenge {
        val challenge = challengeRepository.findById(dailyChallenge.challengeId)
        if (challenge.isEmpty) {
            throw IllegalArgumentException("Challenge with ID ${dailyChallenge.challengeId} does not exist")
        }

        val existingDailyChallenge = dailyChallengeRepository.findByDate(dailyChallenge.date)
        if (existingDailyChallenge != null && existingDailyChallenge.id != dailyChallenge.id) {
            val updatedExisting = existingDailyChallenge.copy(isActive = false)
            dailyChallengeRepository.save(updatedExisting)
            logger.info("기존 일일 챌린지 비활성화: id = {}, date = {}", existingDailyChallenge.id, existingDailyChallenge.date)
        }

        logger.info("일일 챌린지 저장 중: 챌린지 ID = {}, 날짜 = {}", dailyChallenge.challengeId, dailyChallenge.date)
        return dailyChallengeRepository.save(dailyChallenge)
    }

    fun update(
        id: String,
        updatedDailyChallenge: DailyChallenge,
    ): DailyChallenge? {
        val existingDailyChallenge = dailyChallengeRepository.findById(id).orElse(null)

        return if (existingDailyChallenge != null) {
            val challenge = challengeRepository.findById(updatedDailyChallenge.challengeId)
            if (challenge.isEmpty) {
                throw IllegalArgumentException("Challenge with ID ${updatedDailyChallenge.challengeId} does not exist")
            }

            val dailyChallenge =
                updatedDailyChallenge.copy(
                    id = existingDailyChallenge.id,
                )
            dailyChallengeRepository.save(dailyChallenge)
        } else {
            null
        }
    }

    fun deleteById(id: String) {
        dailyChallengeRepository.deleteById(id)
    }

    fun setDailyChallenge(
        date: LocalDate,
        challengeId: String,
    ): DailyChallenge {
        val challenge = challengeRepository.findById(challengeId)
        if (challenge.isEmpty) {
            throw IllegalArgumentException("Challenge with ID $challengeId does not exist")
        }

        val existingDailyChallenge = dailyChallengeRepository.findByDate(date)

        return if (existingDailyChallenge != null) {
            val updatedDailyChallenge =
                existingDailyChallenge.copy(
                    challengeId = challengeId,
                    isActive = true,
                )
            logger.info("기존 일일 챌린지 업데이트: id = {}, date = {}", existingDailyChallenge.id, date)
            dailyChallengeRepository.save(updatedDailyChallenge)
        } else {
            val newDailyChallenge =
                DailyChallenge(
                    challengeId = challengeId,
                    date = date,
                    isActive = true,
                )
            logger.info("새 일일 챌린지 생성: 날짜 = {}", date)
            dailyChallengeRepository.save(newDailyChallenge)
        }
    }

    fun selectRandomChallenge(excludeRecentDays: Int = 7): Challenge? {
        val allChallenges = challengeRepository.findAll()
        if (allChallenges.isEmpty()) {
            logger.warn("사용 가능한 챌린지가 없습니다.")
            return null
        }

        val today = LocalDate.now()
        val startDate = today.minusDays(excludeRecentDays.toLong())
        val recentDailyChallenges = findByDateRange(startDate, today)
        val recentChallengeIds = recentDailyChallenges.map { it.challengeId }.toSet()

        val availableChallenges =
            allChallenges.filter { challenge ->
                challenge.id != null && challenge.id !in recentChallengeIds
            }

        if (availableChallenges.isEmpty()) {
            logger.warn(
                "최근 {}일 동안 사용되지 않은 챌린지가 없습니다. 모든 챌린지에서 랜덤 선택합니다.",
                excludeRecentDays,
            )
            return allChallenges[Random.nextInt(allChallenges.size)]
        }

        val randomIndex = Random.nextInt(availableChallenges.size)
        return availableChallenges[randomIndex]
    }

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
