package lab.ujumeonji.chatalgoapi.repository

import lab.ujumeonji.chatalgoapi.model.DailyChallenge
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface DailyChallengeRepository : MongoRepository<DailyChallenge, String> {
    fun findByDate(date: LocalDate): DailyChallenge?

    fun findByDateBetween(
        startDate: LocalDate,
        endDate: LocalDate,
    ): List<DailyChallenge>

    fun findByIsActiveTrue(): List<DailyChallenge>

    fun findByChallengeId(challengeId: String): List<DailyChallenge>
}
