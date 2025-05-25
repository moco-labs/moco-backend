package lab.ujumeonji.chatalgoapi.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "users")
@TypeAlias("User")
data class User(
    @Id
    val id: String? = null,
    val name: String,
    @Indexed(unique = true)
    val email: String,
    val password: String,
    val authProvider: String? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
)
