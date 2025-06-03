package lab.ujumeonji.moco.model.challenge

import lab.ujumeonji.moco.adapter.DailyChallengeRepositoryAdapter
import lab.ujumeonji.moco.model.DailyChallenge
import lab.ujumeonji.moco.model.challenge.io.CreateDailyChallengeInput
import lab.ujumeonji.moco.model.challenge.io.DailyChallengeOutput
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDate
import kotlin.random.Random

@Service
class DailyChallengeService(
    private val dailyChallengeRepositoryAdapter: DailyChallengeRepositoryAdapter,
    private val challengeService: ChallengeService,
) {
    private val logger = LoggerFactory.getLogger(DailyChallengeService::class.java)

    fun findAll(): List<DailyChallenge> = dailyChallengeRepositoryAdapter.findAll()

    fun findAllOutput(): List<DailyChallengeOutput> = findAll().map { DailyChallengeOutput.fromDomain(it) }

    fun findAll(pageable: Pageable): Page<DailyChallenge> {
        val dailyChallenges = findAll()
        val start = pageable.offset.toInt()
        val end = (start + pageable.pageSize).coerceAtMost(dailyChallenges.size)
        val pageContent = if (start < end) dailyChallenges.subList(start, end) else emptyList()
        return PageImpl(pageContent, pageable, dailyChallenges.size.toLong())
    }

    fun findAllOutput(pageable: Pageable): Page<DailyChallengeOutput> {
        val dailyChallengePage = findAll(pageable)
        return PageImpl(
            dailyChallengePage.content.map { DailyChallengeOutput.fromDomain(it) },
            dailyChallengePage.pageable,
            dailyChallengePage.totalElements,
        )
    }

    fun findById(id: String): DailyChallenge? = dailyChallengeRepositoryAdapter.findById(id).orElse(null)

    fun findByIdOutput(id: String): DailyChallengeOutput? = findById(id)?.let { DailyChallengeOutput.fromDomain(it) }

    fun findBetweenDates(
        startDate: LocalDate,
        endDate: LocalDate,
    ): List<DailyChallenge> = dailyChallengeRepositoryAdapter.findByDateBetween(startDate, endDate)

    fun findBetweenDatesOutput(
        startDate: LocalDate,
        endDate: LocalDate,
    ): List<DailyChallengeOutput> = findBetweenDates(startDate, endDate).map { DailyChallengeOutput.fromDomain(it) }

    fun findBetweenDates(
        startDate: LocalDate,
        endDate: LocalDate,
        pageable: Pageable,
    ): Page<DailyChallenge> {
        val dailyChallenges = findBetweenDates(startDate, endDate)
        val start = pageable.offset.toInt()
        val end = (start + pageable.pageSize).coerceAtMost(dailyChallenges.size)
        val pageContent = if (start < end) dailyChallenges.subList(start, end) else emptyList()
        return PageImpl(pageContent, pageable, dailyChallenges.size.toLong())
    }

    fun findBetweenDatesOutput(
        startDate: LocalDate,
        endDate: LocalDate,
        pageable: Pageable,
    ): Page<DailyChallengeOutput> {
        val dailyChallengePage = findBetweenDates(startDate, endDate, pageable)
        return PageImpl(
            dailyChallengePage.content.map { DailyChallengeOutput.fromDomain(it) },
            dailyChallengePage.pageable,
            dailyChallengePage.totalElements,
        )
    }

    fun findByDateRange(
        startDate: LocalDate,
        endDate: LocalDate,
    ): List<DailyChallenge> = dailyChallengeRepositoryAdapter.findByDateBetween(startDate, endDate)

    fun findByDateRangeOutput(
        startDate: LocalDate,
        endDate: LocalDate,
    ): List<DailyChallengeOutput> = findByDateRange(startDate, endDate).map { DailyChallengeOutput.fromDomain(it) }

    fun findByDateRange(
        startDate: LocalDate,
        endDate: LocalDate,
        pageable: Pageable,
    ): Page<DailyChallenge> {
        val dailyChallenges = findByDateRange(startDate, endDate)
        val start = pageable.offset.toInt()
        val end = (start + pageable.pageSize).coerceAtMost(dailyChallenges.size)
        val pageContent = if (start < end) dailyChallenges.subList(start, end) else emptyList()
        return PageImpl(pageContent, pageable, dailyChallenges.size.toLong())
    }

    fun findByDateRangeOutput(
        startDate: LocalDate,
        endDate: LocalDate,
        pageable: Pageable,
    ): Page<DailyChallengeOutput> {
        val dailyChallengePage = findByDateRange(startDate, endDate, pageable)
        return PageImpl(
            dailyChallengePage.content.map { DailyChallengeOutput.fromDomain(it) },
            dailyChallengePage.pageable,
            dailyChallengePage.totalElements,
        )
    }

    fun findChallengeForDate(date: LocalDate): Challenge? {
        val dailyChallenge = dailyChallengeRepositoryAdapter.findByDate(date)
        return if (dailyChallenge != null) {
            challengeService.findById(dailyChallenge.challengeId)
        } else {
            null
        }
    }

    fun findChallengeOutputForDate(date: LocalDate): lab.ujumeonji.moco.service.challenge.io.ChallengeOutput? {
        val challenge = findChallengeForDate(date)
        return challenge?.let { lab.ujumeonji.moco.service.challenge.io.ChallengeOutput.fromDomain(it) }
    }

    fun save(input: CreateDailyChallengeInput): DailyChallenge {
        val challenge = challengeService.findById(input.challengeId)
        if (challenge == null) {
            throw IllegalArgumentException("Challenge with ID ${input.challengeId} does not exist")
        }

        val existingDailyChallenge = dailyChallengeRepositoryAdapter.findByDate(input.date)
        if (existingDailyChallenge != null) {
            val updatedExisting =
                existingDailyChallenge.copy(
                    date = existingDailyChallenge.date,
                    isActive = false,
                )
            dailyChallengeRepositoryAdapter.save(updatedExisting)
            logger.info("Deactivating existing daily challenge: id = {}, date = {}", existingDailyChallenge.id, existingDailyChallenge.date)
        }

        logger.info("Saving daily challenge: Challenge ID = {}, Date = {}", input.challengeId, input.date)
        return dailyChallengeRepositoryAdapter.save(input.toDomain())
    }

    fun save(dailyChallenge: DailyChallenge): DailyChallenge {
        val challenge = challengeService.findById(dailyChallenge.challengeId)
        if (challenge == null) {
            throw IllegalArgumentException("Challenge with ID ${dailyChallenge.challengeId} does not exist")
        }

        val existingDailyChallenge = dailyChallengeRepositoryAdapter.findByDate(dailyChallenge.date)
        if (existingDailyChallenge != null && existingDailyChallenge.id != dailyChallenge.id) {
            val updatedExisting =
                existingDailyChallenge.copy(
                    date = existingDailyChallenge.date,
                    isActive = false,
                )
            dailyChallengeRepositoryAdapter.save(updatedExisting)
            logger.info("Deactivating existing daily challenge: id = {}, date = {}", existingDailyChallenge.id, existingDailyChallenge.date)
        }

        logger.info("Saving daily challenge: Challenge ID = {}, Date = {}", dailyChallenge.challengeId, dailyChallenge.date)
        return dailyChallengeRepositoryAdapter.save(dailyChallenge)
    }

    fun saveOutput(input: CreateDailyChallengeInput): DailyChallengeOutput {
        val dailyChallenge = save(input)
        return DailyChallengeOutput.fromDomain(dailyChallenge)
    }

    fun setDailyChallenge(
        date: LocalDate,
        challengeId: String,
    ): DailyChallenge {
        val challenge = challengeService.findById(challengeId)
        if (challenge == null) {
            throw IllegalArgumentException("Challenge with ID $challengeId does not exist")
        }

        val existingDailyChallenge = dailyChallengeRepositoryAdapter.findByDate(date)

        return if (existingDailyChallenge != null) {
            existingDailyChallenge.changeChallenge(challenge)
            logger.info("기존 일일 챌린지 업데이트: id = {}, date = {}", existingDailyChallenge.id, date)
            dailyChallengeRepositoryAdapter.save(existingDailyChallenge)
        } else {
            val newDailyChallenge =
                DailyChallenge.create(
                    challenge = challenge,
                    date = date,
                )
            logger.info("새 일일 챌린지 생성: 날짜 = {}", date)
            dailyChallengeRepositoryAdapter.save(newDailyChallenge)
        }
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

    private fun selectRandomChallenge(excludeRecentDays: Int = 7): Challenge? {
        val allChallenges = challengeService.findAll()
        if (allChallenges.isEmpty()) {
            logger.warn("사용 가능한 챌린지가 없습니다.")
            return null
        }

        val today = LocalDate.now()
        val startDate = today.minusDays(excludeRecentDays.toLong())
        val recentDailyChallenges = findBetweenDates(startDate, today)
        val recentChallengeIds = recentDailyChallenges.map { it.challengeId }.toSet()

        val availableChallenges =
            allChallenges.filter { challenge ->
                challenge.id !in recentChallengeIds
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
}
