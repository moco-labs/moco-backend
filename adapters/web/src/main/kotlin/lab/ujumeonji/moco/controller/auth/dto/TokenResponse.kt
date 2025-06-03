package lab.ujumeonji.moco.controller.auth.dto

import lab.ujumeonji.moco.model.user.io.TokenOutput

data class TokenResponse(
    val success: Boolean,
    val message: String,
    val accessToken: String? = null,
    val userId: String? = null,
    val name: String? = null,
    val email: String? = null,
) {
    companion object {
        fun from(output: TokenOutput): TokenResponse {
            return TokenResponse(
                success = output.success,
                message = output.message,
                accessToken = output.accessToken,
                userId = output.userId,
                name = output.name,
                email = output.email,
            )
        }
    }
}
