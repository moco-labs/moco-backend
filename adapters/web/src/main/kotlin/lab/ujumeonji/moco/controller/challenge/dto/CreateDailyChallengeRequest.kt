package lab.ujumeonji.moco.controller.challenge.dto

import jakarta.validation.constraints.NotBlank
import lab.ujumeonji.moco.model.challenge.io.CreateDailyChallengeInput
import java.time.LocalDate

data class CreateDailyChallengeRequest(
    @field:NotBlank(message = "Challenge ID is required")
    val challengeId: String,
    val date: LocalDate = LocalDate.now(),
    val isActive: Boolean = true,
) {
    fun toInput(): CreateDailyChallengeInput {
        return CreateDailyChallengeInput(
            challengeId = challengeId,
            date = date,
            isActive = isActive,
        )
    }
}
