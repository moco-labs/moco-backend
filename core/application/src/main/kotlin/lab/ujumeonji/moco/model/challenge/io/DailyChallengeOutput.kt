package lab.ujumeonji.moco.model.challenge.io

import lab.ujumeonji.moco.model.DailyChallenge
import java.time.LocalDate

data class DailyChallengeOutput(
    val id: String?,
    val challengeId: String,
    val date: LocalDate,
    val isActive: Boolean,
) {
    companion object {
        fun fromDomain(dailyChallenge: DailyChallenge): DailyChallengeOutput {
            return DailyChallengeOutput(
                id = dailyChallenge.id,
                challengeId = dailyChallenge.challengeId,
                date = dailyChallenge.date,
                isActive = dailyChallenge.isActive,
            )
        }
    }
}
