package lab.ujumeonji.moco.model

import lab.ujumeonji.moco.model.challenge.Challenge
import lab.ujumeonji.moco.model.challenge.ChallengeExample
import lab.ujumeonji.moco.model.challenge.HighlightToken
import org.springframework.stereotype.Component

@Component
class ChallengeMapper {
    fun toDomain(entity: ChallengeEntity): Challenge {
        return Challenge(
            id = entity.id,
            title = entity.title,
            description = entity.description,
            descriptionHighlightTokens = entity.descriptionHighlightTokens.map { it.toDomain() },
            instructions = entity.instructions,
            difficulty = entity.difficulty,
            tags = entity.tags,
            content = entity.content,
            examples = entity.examples.map { it.toDomain() },
            constraints = entity.constraints,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
        )
    }

    fun toEntity(domain: Challenge): ChallengeEntity {
        return ChallengeEntity(
            id = domain.id,
            title = domain.title,
            description = domain.description,
            descriptionHighlightTokens = domain.descriptionHighlightTokens.map { it.toEntity() },
            instructions = domain.instructions,
            difficulty = domain.difficulty,
            tags = domain.tags,
            content = domain.content,
            examples = domain.examples.map { it.toEntity() },
            constraints = domain.constraints,
            createdAt = domain.createdAt,
            updatedAt = domain.updatedAt,
        )
    }

    private fun HighlightTokenEntity.toDomain(): HighlightToken {
        return HighlightToken(
            token = this.token,
            isHighlighted = this.isHighlighted,
        )
    }

    private fun HighlightToken.toEntity(): HighlightTokenEntity {
        return HighlightTokenEntity(
            token = this.token,
            isHighlighted = this.isHighlighted,
        )
    }

    private fun ChallengeExampleEntity.toDomain(): ChallengeExample {
        return ChallengeExample(
            input = this.input,
            output = this.output,
            explanation = this.explanation,
        )
    }

    private fun ChallengeExample.toEntity(): ChallengeExampleEntity {
        return ChallengeExampleEntity(
            input = this.input,
            output = this.output,
            explanation = this.explanation,
        )
    }
}
