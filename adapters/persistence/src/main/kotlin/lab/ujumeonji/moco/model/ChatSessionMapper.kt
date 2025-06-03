package lab.ujumeonji.moco.model

import lab.ujumeonji.moco.model.challenge.ChatSession
import lab.ujumeonji.moco.model.challenge.Message
import org.springframework.stereotype.Component

@Component
class ChatSessionMapper {
    fun toDomain(entity: ChatSessionEntity): ChatSession {
        return ChatSession(
            id = entity.id,
            challengeId = entity.challengeId,
            userId = entity.userId,
            messages = entity.messages.map { it.toDomain() }.toMutableList(),
            understandingScore = entity.understandingScore,
            interactionCount = entity.interactionCount,
            maxInteractions = entity.maxInteractions,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
        )
    }

    fun toEntity(domain: ChatSession): ChatSessionEntity {
        return ChatSessionEntity(
            id = domain.id,
            challengeId = domain.challengeId,
            userId = domain.userId,
            messages = domain.messages.map { it.toEntity() }.toMutableList(),
            understandingScore = domain.understandingScore,
            interactionCount = domain.interactionCount,
            maxInteractions = domain.maxInteractions,
            createdAt = domain.createdAt,
            updatedAt = domain.updatedAt,
        )
    }

    private fun MessageEntity.toDomain(): Message {
        return Message(
            content = this.content,
            sender = this.sender,
            timestamp = this.timestamp,
        )
    }

    private fun Message.toEntity(): MessageEntity {
        return MessageEntity(
            content = this.content,
            sender = this.sender,
            timestamp = this.timestamp,
        )
    }
}
