package lab.ujumeonji.chatalgoapi.dto

data class TokenResponse(
    val success: Boolean,
    val message: String,
    val accessToken: String? = null,
    val userId: String? = null,
    val name: String? = null,
    val email: String? = null,
)