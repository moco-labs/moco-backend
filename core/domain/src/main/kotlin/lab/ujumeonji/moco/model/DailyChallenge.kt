package lab.ujumeonji.moco.model

import lab.ujumeonji.moco.model.challenge.Challenge
import java.time.LocalDate

data class DailyChallenge(
    val id: String? = null,
    var challengeId: String,
    val date: LocalDate,
    val isActive: Boolean = true,
) {
    fun changeChallenge(challenge: Challenge) {
        challengeId = challenge.id
    }

    companion object {
        fun create(
            challenge: Challenge,
            date: LocalDate = LocalDate.now(),
            isActive: Boolean = true,
        ): DailyChallenge =
            DailyChallenge(
                challengeId = challenge.id,
                date = date,
                isActive = isActive,
            )
    }
}
