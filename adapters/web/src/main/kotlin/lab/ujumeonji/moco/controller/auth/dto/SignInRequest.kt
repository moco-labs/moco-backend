package lab.ujumeonji.moco.controller.auth.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import lab.ujumeonji.moco.model.user.io.SignInInput

data class SignInRequest(
    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Invalid email format")
    val email: String,
    @field:NotBlank(message = "Password is required")
    val password: String,
) {
    fun toInput(): SignInInput {
        return SignInInput(
            email = email,
            password = password,
        )
    }
}
