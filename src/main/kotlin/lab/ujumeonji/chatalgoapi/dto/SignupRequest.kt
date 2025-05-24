package lab.ujumeonji.chatalgoapi.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class SignupRequest(
    @field:NotBlank(message = "Name is required")
    val name: String,
    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Invalid email format")
    val email: String,
    @field:NotBlank(message = "Password is required")
    @field:Size(min = 6, message = "Password must be at least 6 characters")
    val password: String,
)
