package lab.ujumeonji.moco.controller.auth.dto

import lab.ujumeonji.moco.model.user.io.AuthOutput

data class AuthResponse(
    val success: Boolean,
    val message: String,
    val userId: String? = null,
    val email: String? = null,
    val name: String? = null,
) {
    companion object {
        fun from(output: AuthOutput): AuthResponse {
            return AuthResponse(
                success = output.success,
                message = output.message,
                userId = output.userId,
                email = output.email,
                name = output.name
            )
        }
    }
}