package lab.ujumeonji.moco.service.challenge.io

import lab.ujumeonji.moco.model.challenge.Challenge
import java.time.LocalDateTime

data class ChallengeOutput(
    val id: String,
    val title: String,
    val description: String,
    val descriptionHighlightTokens: List<HighlightTokenOutput>,
    val instructions: String,
    val difficulty: String,
    val tags: List<String>,
    val content: String?,
    val examples: List<ChallengeExampleOutput>,
    val constraints: List<String>,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {
    data class HighlightTokenOutput(
        val token: String,
        val isHighlighted: Boolean,
    )

    data class ChallengeExampleOutput(
        val input: String,
        val output: String,
        val explanation: String,
    )

    companion object {
        fun fromDomain(challenge: Challenge): ChallengeOutput {
            return ChallengeOutput(
                id = challenge.id,
                title = challenge.title,
                description = challenge.description,
                descriptionHighlightTokens =
                    challenge.descriptionHighlightTokens.map {
                        HighlightTokenOutput(
                            token = it.token,
                            isHighlighted = it.isHighlighted,
                        )
                    },
                instructions = challenge.instructions,
                difficulty = challenge.difficulty,
                tags = challenge.tags,
                content = challenge.content,
                examples =
                    challenge.examples.map {
                        ChallengeExampleOutput(
                            input = it.input,
                            output = it.output,
                            explanation = it.explanation,
                        )
                    },
                constraints = challenge.constraints,
                createdAt = challenge.createdAt,
                updatedAt = challenge.updatedAt,
            )
        }
    }
}
