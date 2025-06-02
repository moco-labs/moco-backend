package lab.ujumeonji.moco.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import java.util.UUID

@Document(collection = "challenges")
@TypeAlias("Challenge")
data class ChallengeEntity(
    @Id
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val descriptionHighlightTokens: List<HighlightTokenEntity>,
    val instructions: String,
    val difficulty: String,
    val tags: List<String>,
    val content: String? = null,
    val examples: List<ChallengeExampleEntity> = emptyList(),
    val constraints: List<String> = emptyList(),
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
)

data class ChallengeExampleEntity(
    val input: String,
    val output: String,
    val explanation: String,
)

data class HighlightTokenEntity(
    val token: String,
    val isHighlighted: Boolean,
)
