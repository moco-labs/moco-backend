package lab.ujumeonji.moco.controller.challenge.dto

import lab.ujumeonji.moco.model.challenge.io.DailyChallengeOutput
import java.time.LocalDate

data class DailyChallengeResponse(
    val id: String?,
    val challengeId: String,
    val date: LocalDate,
    val isActive: Boolean,
) {
    companion object {
        fun from(output: DailyChallengeOutput): DailyChallengeResponse {
            return DailyChallengeResponse(
                id = output.id,
                challengeId = output.challengeId,
                date = output.date,
                isActive = output.isActive
            )
        }
    }
}