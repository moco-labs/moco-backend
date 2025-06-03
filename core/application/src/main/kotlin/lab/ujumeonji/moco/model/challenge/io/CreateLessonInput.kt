package lab.ujumeonji.moco.model.challenge.io

import lab.ujumeonji.moco.model.challenge.Lesson
import lab.ujumeonji.moco.model.challenge.LessonSection
import lab.ujumeonji.moco.model.challenge.SectionType

data class CreateLessonInput(
    val challengeId: String,
    val sections: List<CreateLessonSectionInput> = emptyList(),
) {
    data class CreateLessonSectionInput(
        val title: String,
        val type: SectionType,
        val data: Any? = null,
    )

    internal fun toDomain(): Lesson {
        return Lesson(
            challengeId = challengeId,
            sections = sections.map { 
                LessonSection(
                    title = it.title,
                    type = it.type,
                    data = it.data
                )
            }
        )
    }
}