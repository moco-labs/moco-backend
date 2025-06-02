package lab.ujumeonji.moco.model.challenge

import java.time.LocalDateTime
import java.util.UUID

class Challenge(
    val id: String = UUID.randomUUID().toString(),
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
    val updatedAt: LocalDateTime = LocalDateTime.now(),
)

class ChallengeExample(
    val input: String,
    val output: String,
    val explanation: String,
)

class HighlightToken(
    val token: String,
    val isHighlighted: Boolean,
)
