package lab.ujumeonji.moco.model

import java.time.LocalDate

class DailyChallenge(
    val id: String? = null,
    val challengeId: String,
    val date: LocalDate,
    val isActive: Boolean = true,
) {
    companion object {
        fun create(
            id: String? = null,
            challengeId: String,
            date: LocalDate = LocalDate.now(),
            isActive: Boolean = true,
        ): DailyChallenge =
            DailyChallenge(
                id = id,
                challengeId = challengeId,
                date = date,
                isActive = isActive,
            )
    }
}
