package lab.ujumeonji.moco.model.user.io

import lab.ujumeonji.moco.model.user.User
import java.time.LocalDateTime

data class UserOutput(
    val id: String,
    val name: String,
    val email: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {
    companion object {
        fun fromDomain(user: User): UserOutput =
            UserOutput(
                id = user.id,
                name = user.name,
                email = user.email,
                createdAt = user.createdAt,
                updatedAt = user.updatedAt,
            )
    }
}
