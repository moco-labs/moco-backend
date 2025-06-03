package lab.ujumeonji.moco.model.challenge.io

import lab.ujumeonji.moco.model.challenge.Challenge
import lab.ujumeonji.moco.model.challenge.ChallengeExample
import lab.ujumeonji.moco.model.challenge.HighlightToken

data class CreateChallengeInput(
    val title: String,
    val description: String,
    val descriptionHighlightTokens: List<CreateHighlightTokenInput>,
    val instructions: String,
    val difficulty: String,
    val tags: List<String>,
    val content: String? = null,
    val examples: List<CreateChallengeExampleInput> = emptyList(),
    val constraints: List<String> = emptyList(),
) {
    internal fun toDomain(): Challenge {
        return Challenge(
            title = title,
            description = description,
            descriptionHighlightTokens = descriptionHighlightTokens.map { it.toDomain() },
            instructions = instructions,
            difficulty = difficulty,
            tags = tags,
            content = content,
            examples = examples.map { it.toDomain() },
            constraints = constraints,
        )
    }
}

data class CreateHighlightTokenInput(
    val token: String,
    val isHighlighted: Boolean,
) {
    internal fun toDomain(): HighlightToken {
        return HighlightToken(
            token = token,
            isHighlighted = isHighlighted,
        )
    }
}

data class CreateChallengeExampleInput(
    val input: String,
    val output: String,
    val explanation: String,
) {
    internal fun toDomain(): ChallengeExample {
        return ChallengeExample(
            input = input,
            output = output,
            explanation = explanation,
        )
    }
}
