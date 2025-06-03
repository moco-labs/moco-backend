package lab.ujumeonji.moco.controller.challenge.dto

import lab.ujumeonji.moco.service.challenge.io.ChallengeOutput
import java.time.LocalDateTime

data class ChallengeResponse(
    val id: String,
    val title: String,
    val description: String,
    val descriptionHighlightTokens: List<HighlightTokenResponse>,
    val instructions: String,
    val difficulty: String,
    val tags: List<String>,
    val content: String?,
    val examples: List<ChallengeExampleResponse>,
    val constraints: List<String>,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {
    data class HighlightTokenResponse(
        val token: String,
        val isHighlighted: Boolean,
    )

    data class ChallengeExampleResponse(
        val input: String,
        val output: String,
        val explanation: String,
    )

    companion object {
        fun from(output: ChallengeOutput): ChallengeResponse {
            return ChallengeResponse(
                id = output.id,
                title = output.title,
                description = output.description,
                descriptionHighlightTokens =
                    output.descriptionHighlightTokens.map {
                        HighlightTokenResponse(
                            token = it.token,
                            isHighlighted = it.isHighlighted,
                        )
                    },
                instructions = output.instructions,
                difficulty = output.difficulty,
                tags = output.tags,
                content = output.content,
                examples =
                    output.examples.map {
                        ChallengeExampleResponse(
                            input = it.input,
                            output = it.output,
                            explanation = it.explanation,
                        )
                    },
                constraints = output.constraints,
                createdAt = output.createdAt,
                updatedAt = output.updatedAt,
            )
        }
    }
}
