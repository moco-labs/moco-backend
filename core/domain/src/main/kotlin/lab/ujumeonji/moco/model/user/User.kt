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
)
