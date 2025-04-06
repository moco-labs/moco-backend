package lab.ujumeonji.chatalgoapi.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "challenges")
data class Challenge(
    @Id
    val id: String? = null,
    val title: String,
    val description: String,
    val descriptionHighlightTokens: List<HighlightToken>,
    val instructions: String,
    val difficulty: String,
    val tags: List<String>,
    val content: String? = null,
    val examples: List<ChallengeExample> = emptyList(),
    val constraints: List<String> = emptyList(),
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)

data class ChallengeExample(
    val input: String,
    val output: String,
    val explanation: String,
)

data class HighlightToken(
    val token: String,
    val isHighlighted: Boolean,
)
