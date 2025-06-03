package lab.ujumeonji.moco.model.challenge.io

import lab.ujumeonji.moco.model.DailyChallenge
import java.time.LocalDate

data class CreateDailyChallengeInput(
    val challengeId: String,
    val date: LocalDate = LocalDate.now(),
    val isActive: Boolean = true,
) {
    internal fun toDomain(): DailyChallenge {
        return DailyChallenge(
            challengeId = challengeId,
            date = date,
            isActive = isActive
        )
    }
}