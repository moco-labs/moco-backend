package lab.ujumeonji.moco.controller.auth.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import lab.ujumeonji.moco.model.user.io.SignUpInput

data class SignUpRequest(
    @field:NotBlank(message = "Name is required")
    val name: String,

    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Invalid email format")
    val email: String,

    @field:NotBlank(message = "Password is required")
    @field:Size(min = 6, message = "Password must be at least 6 characters")
    val password: String,
) {
    fun toInput(): SignUpInput {
        return SignUpInput(
            name = name,
            email = email,
            password = password
        )
    }
}
