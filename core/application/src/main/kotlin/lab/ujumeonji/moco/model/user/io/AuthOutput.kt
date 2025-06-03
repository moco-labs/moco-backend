package lab.ujumeonji.moco.model.user.io

data class AuthOutput(
    val success: Boolean,
    val message: String,
    val userId: String? = null,
    val email: String? = null,
    val name: String? = null,
)
