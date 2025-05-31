package lab.ujumeonji.moco.model

import org.springframework.stereotype.Component

@Component
class DailyChallengeMapper {
    fun toDomain(entity: DailyChallengeEntity): DailyChallenge {
        return DailyChallenge(
            id = entity.id,
            challengeId = entity.challengeId,
            date = entity.date,
            isActive = entity.isActive,
        )
    }

    fun toEntity(domain: DailyChallenge): DailyChallengeEntity {
        return DailyChallengeEntity(
            id = domain.id,
            challengeId = domain.challengeId,
            date = domain.date,
            isActive = domain.isActive,
        )
    }
}
