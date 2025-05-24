package lab.ujumeonji.chatalgoapi.dto

data class AuthResponse(
    val success: Boolean,
    val message: String,
    val userId: String? = null,
    val email: String? = null,
    val name: String? = null,
)
