package lab.ujumeonji.moco.model

import lab.ujumeonji.moco.model.challenge.Challenge
import java.time.LocalDate

class DailyChallenge(
    val id: String? = null,
    val challengeId: String,
    val date: LocalDate,
    val isActive: Boolean = true,
) {
    companion object {
        fun create(
            challenge: Challenge,
            date: LocalDate = LocalDate.now(),
            isActive: Boolean = true,
        ): DailyChallenge =
            DailyChallenge(
                challengeId = challenge.id!!,
                date = date,
                isActive = isActive,
            )
    }
}
