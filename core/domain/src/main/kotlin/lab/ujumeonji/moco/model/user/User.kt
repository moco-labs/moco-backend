package lab.ujumeonji.moco.model.user

import java.time.LocalDateTime
import java.util.UUID

class User(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val email: String,
    val password: String,
    val authProvider: String? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
) {
    init {
        require(name.isNotBlank()) { "Name must not be blank" }
        require(email.isNotBlank()) { "Email must not be blank" }
        require(EMAIL_REGEX.matches(email)) { "Invalid email format" }
    }

    companion object {
        private val EMAIL_REGEX = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")

        fun signUp(
            name: String,
            email: String,
            password: String,
            now: LocalDateTime = LocalDateTime.now(),
        ): User =
            User(
                id = UUID.randomUUID().toString(),
                name = name,
                email = email,
                password = password,
                authProvider = null,
                createdAt = now,
                updatedAt = now,
            )
    }
}
