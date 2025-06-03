package lab.ujumeonji.moco.controller.challenge.dto

import lab.ujumeonji.moco.model.challenge.SectionType
import lab.ujumeonji.moco.model.challenge.io.LessonOutput
import java.time.LocalDateTime

data class LessonResponse(
    val id: String?,
    val challengeId: String,
    val sections: List<LessonSectionResponse> = emptyList(),
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {
    data class LessonSectionResponse(
        val title: String,
        val type: SectionType,
        val data: Any? = null,
    )

    companion object {
        fun from(output: LessonOutput): LessonResponse {
            return LessonResponse(
                id = output.id,
                challengeId = output.challengeId,
                sections = output.sections.map { 
                    LessonSectionResponse(
                        title = it.title,
                        type = it.type,
                        data = it.data
                    )
                },
                createdAt = output.createdAt,
                updatedAt = output.updatedAt
            )
        }
    }
}