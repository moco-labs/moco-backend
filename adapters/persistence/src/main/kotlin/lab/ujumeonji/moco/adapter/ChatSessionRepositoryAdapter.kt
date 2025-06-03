package lab.ujumeonji.moco.adapter

import lab.ujumeonji.moco.model.ChatSessionMapper
import lab.ujumeonji.moco.model.challenge.ChatSession
import lab.ujumeonji.moco.repository.ChatSessionRepository
import org.springframework.stereotype.Component
import java.util.Optional

@Component
class ChatSessionRepositoryAdapter(
    private val chatSessionRepository: ChatSessionRepository,
    private val chatSessionMapper: ChatSessionMapper,
) {
    fun findAll(): List<ChatSession> {
        return chatSessionRepository.findAll().map { chatSessionMapper.toDomain(it) }
    }

    fun findById(id: String): Optional<ChatSession> {
        return chatSessionRepository.findById(id).map { chatSessionMapper.toDomain(it) }
    }

    fun findByIdAndUserId(
        id: String,
        userId: String,
    ): ChatSession? {
        return chatSessionRepository.findByIdAndUserId(id, userId)?.let { chatSessionMapper.toDomain(it) }
    }

    fun findByChallengeIdAndUserId(
        challengeId: String,
        userId: String,
    ): ChatSession? {
        return chatSessionRepository.findByChallengeIdAndUserId(challengeId, userId)?.let { chatSessionMapper.toDomain(it) }
    }

    fun findByUserId(userId: String): List<ChatSession> {
        return chatSessionRepository.findByUserId(userId).map { chatSessionMapper.toDomain(it) }
    }

    fun save(chatSession: ChatSession): ChatSession {
        val entity = chatSessionMapper.toEntity(chatSession)
        val savedEntity = chatSessionRepository.save(entity)
        return chatSessionMapper.toDomain(savedEntity)
    }

    fun deleteById(id: String) {
        chatSessionRepository.deleteById(id)
    }
}
