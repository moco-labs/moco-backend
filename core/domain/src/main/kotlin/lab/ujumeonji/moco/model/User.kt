package lab.ujumeonji.moco.model

import java.time.LocalDateTime

class User(
    val id: String? = null,
    val name: String,
    val email: String,
    val password: String,
    val authProvider: String? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
)
