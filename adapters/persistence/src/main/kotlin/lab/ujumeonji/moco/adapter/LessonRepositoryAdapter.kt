package lab.ujumeonji.moco.adapter

import lab.ujumeonji.moco.model.LessonMapper
import lab.ujumeonji.moco.model.SectionTypeEntity
import lab.ujumeonji.moco.model.challenge.Lesson
import lab.ujumeonji.moco.model.challenge.SectionType
import lab.ujumeonji.moco.repository.LessonRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import java.util.Optional

@Component
class LessonRepositoryAdapter(
    private val lessonRepository: LessonRepository,
    private val lessonMapper: LessonMapper,
) {
    fun findAll(): List<Lesson> {
        return lessonRepository.findAll().map { lessonMapper.toDomain(it) }
    }

    fun findAll(pageable: Pageable): Page<Lesson> {
        return lessonRepository.findAll(pageable).map { lessonMapper.toDomain(it) }
    }

    fun findById(id: String): Optional<Lesson> {
        return lessonRepository.findById(id).map { lessonMapper.toDomain(it) }
    }

    fun findByChallengeId(
        challengeId: String,
        pageable: Pageable,
    ): Page<Lesson> {
        return lessonRepository.findByChallengeId(challengeId, pageable).map { lessonMapper.toDomain(it) }
    }

    fun findBySectionsType(
        type: SectionType,
        pageable: Pageable,
    ): Page<Lesson> {
        val entityType = type.toEntity()
        return lessonRepository.findBySectionsType(entityType, pageable).map { lessonMapper.toDomain(it) }
    }

    fun findByChallengeIdAndSectionsType(
        challengeId: String,
        type: SectionType,
        pageable: Pageable,
    ): Page<Lesson> {
        val entityType = type.toEntity()
        return lessonRepository.findByChallengeIdAndSectionsType(challengeId, entityType, pageable)
            .map { lessonMapper.toDomain(it) }
    }

    fun save(lesson: Lesson): Lesson {
        val entity = lessonMapper.toEntity(lesson)
        val savedEntity = lessonRepository.save(entity)
        return lessonMapper.toDomain(savedEntity)
    }

    private fun SectionType.toEntity(): SectionTypeEntity {
        return when (this) {
            SectionType.GAP_FILL -> SectionTypeEntity.GAP_FILL
            SectionType.IMPLEMENTATION -> SectionTypeEntity.IMPLEMENTATION
            SectionType.TEXT -> SectionTypeEntity.TEXT
        }
    }
}
