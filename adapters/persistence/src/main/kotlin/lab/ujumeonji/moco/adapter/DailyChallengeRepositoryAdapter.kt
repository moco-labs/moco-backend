package lab.ujumeonji.moco.adapter

import lab.ujumeonji.moco.model.DailyChallenge
import lab.ujumeonji.moco.model.DailyChallengeMapper
import lab.ujumeonji.moco.repository.DailyChallengeRepository
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.Optional

@Component
class DailyChallengeRepositoryAdapter(
    private val dailyChallengeRepository: DailyChallengeRepository,
    private val dailyChallengeMapper: DailyChallengeMapper,
) {
    fun findAll(): List<DailyChallenge> {
        return dailyChallengeRepository.findAll().map { dailyChallengeMapper.toDomain(it) }
    }

    fun findById(id: String): Optional<DailyChallenge> {
        return dailyChallengeRepository.findById(id).map { dailyChallengeMapper.toDomain(it) }
    }

    fun findByDate(date: LocalDate): DailyChallenge? {
        return dailyChallengeRepository.findByDate(date)?.let { dailyChallengeMapper.toDomain(it) }
    }

    fun findByDateBetween(
        startDate: LocalDate,
        endDate: LocalDate,
    ): List<DailyChallenge> {
        return dailyChallengeRepository.findByDateBetween(startDate, endDate).map { dailyChallengeMapper.toDomain(it) }
    }

    fun save(dailyChallenge: DailyChallenge): DailyChallenge {
        val entity = dailyChallengeMapper.toEntity(dailyChallenge)
        val savedEntity = dailyChallengeRepository.save(entity)
        return dailyChallengeMapper.toDomain(savedEntity)
    }
}
