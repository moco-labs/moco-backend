package lab.ujumeonji.moco.service.user.io

data class TokenOutput(
    val success: Boolean,
    val message: String,
    val accessToken: String? = null,
    val userId: String? = null,
    val name: String? = null,
    val email: String? = null,
)
