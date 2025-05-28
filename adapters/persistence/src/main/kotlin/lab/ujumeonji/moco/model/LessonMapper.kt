package lab.ujumeonji.moco.model

import org.springframework.stereotype.Component

@Component
class LessonMapper {
    fun toDomain(entity: LessonEntity): Lesson {
        return Lesson(
            id = entity.id,
            challengeId = entity.challengeId,
            sections = entity.sections.map { it.toDomain() },
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
        )
    }

    fun toEntity(domain: Lesson): LessonEntity {
        return LessonEntity(
            id = domain.id,
            challengeId = domain.challengeId,
            sections = domain.sections.map { it.toEntity() },
            createdAt = domain.createdAt,
            updatedAt = domain.updatedAt,
        )
    }

    private fun LessonSectionEntity.toDomain(): LessonSection {
        return LessonSection(
            title = this.title,
            type = this.type.toDomain(),
            data = this.data,
        )
    }

    private fun LessonSection.toEntity(): LessonSectionEntity {
        return LessonSectionEntity(
            title = this.title,
            type = this.type.toEntity(),
            data = this.data,
        )
    }

    private fun SectionTypeEntity.toDomain(): SectionType {
        return when (this) {
            SectionTypeEntity.GAP_FILL -> SectionType.GAP_FILL
            SectionTypeEntity.IMPLEMENTATION -> SectionType.IMPLEMENTATION
            SectionTypeEntity.TEXT -> SectionType.TEXT
        }
    }

    private fun SectionType.toEntity(): SectionTypeEntity {
        return when (this) {
            SectionType.GAP_FILL -> SectionTypeEntity.GAP_FILL
            SectionType.IMPLEMENTATION -> SectionTypeEntity.IMPLEMENTATION
            SectionType.TEXT -> SectionTypeEntity.TEXT
        }
    }
}
