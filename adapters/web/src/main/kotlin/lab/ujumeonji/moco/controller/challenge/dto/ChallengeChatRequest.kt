package lab.ujumeonji.moco.controller.challenge.dto

import jakarta.validation.constraints.NotBlank
import lab.ujumeonji.moco.service.challenge.io.ChallengeChatInput

data class ChallengeChatRequest(
    @field:NotBlank(message = "Message is required")
    val message: String,
) {
    fun toInput(): ChallengeChatInput {
        return ChallengeChatInput(
            message = message
        )
    }
}