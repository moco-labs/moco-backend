package lab.ujumeonji.moco.model.challenge.io

import lab.ujumeonji.moco.model.challenge.Lesson
import lab.ujumeonji.moco.model.challenge.LessonSection
import lab.ujumeonji.moco.model.challenge.SectionType
import java.time.LocalDateTime

data class LessonOutput(
    val id: String?,
    val challengeId: String,
    val sections: List<LessonSectionOutput> = emptyList(),
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {
    data class LessonSectionOutput(
        val title: String,
        val type: SectionType,
        val data: Any? = null,
    )

    companion object {
        fun fromDomain(lesson: Lesson): LessonOutput {
            return LessonOutput(
                id = lesson.id,
                challengeId = lesson.challengeId,
                sections = lesson.sections.map { 
                    LessonSectionOutput(
                        title = it.title,
                        type = it.type,
                        data = it.data
                    )
                },
                createdAt = lesson.createdAt,
                updatedAt = lesson.updatedAt
            )
        }
    }
}