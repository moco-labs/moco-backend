package lab.ujumeonji.moco.controller.challenge.dto

import lab.ujumeonji.moco.model.challenge.io.CreateChallengeExampleInput
import lab.ujumeonji.moco.model.challenge.io.CreateChallengeInput
import lab.ujumeonji.moco.model.challenge.io.CreateHighlightTokenInput

data class CreateChallengeRequest(
    val title: String,
    val description: String,
    val descriptionHighlightTokens: List<CreateHighlightTokenRequest>,
    val instructions: String,
    val difficulty: String,
    val tags: List<String>,
    val content: String? = null,
    val examples: List<CreateChallengeExampleRequest> = emptyList(),
    val constraints: List<String> = emptyList(),
) {
    fun toInput(): CreateChallengeInput {
        return CreateChallengeInput(
            title = title,
            description = description,
            descriptionHighlightTokens = descriptionHighlightTokens.map { it.toInput() },
            instructions = instructions,
            difficulty = difficulty,
            tags = tags,
            content = content,
            examples = examples.map { it.toInput() },
            constraints = constraints,
        )
    }
}

data class CreateHighlightTokenRequest(
    val token: String,
    val isHighlighted: Boolean,
) {
    fun toInput(): CreateHighlightTokenInput {
        return CreateHighlightTokenInput(
            token = token,
            isHighlighted = isHighlighted,
        )
    }
}

data class CreateChallengeExampleRequest(
    val input: String,
    val output: String,
    val explanation: String,
) {
    fun toInput(): CreateChallengeExampleInput {
        return CreateChallengeExampleInput(
            input = input,
            output = output,
            explanation = explanation,
        )
    }
}
