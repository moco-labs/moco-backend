package lab.ujumeonji.moco.repository

import lab.ujumeonji.moco.model.DailyChallengeEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface DailyChallengeRepository : MongoRepository<DailyChallengeEntity, String> {
    fun findByDate(date: LocalDate): DailyChallengeEntity?

    fun findByDateBetween(
        startDate: LocalDate,
        endDate: LocalDate,
    ): List<DailyChallengeEntity>
}
