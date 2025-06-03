package lab.ujumeonji.moco.controller.challenge.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import lab.ujumeonji.moco.model.challenge.SectionType
import lab.ujumeonji.moco.model.challenge.io.CreateLessonInput

data class CreateLessonRequest(
    @field:NotBlank(message = "Challenge ID is required")
    val challengeId: String,
    @field:NotEmpty(message = "At least one section is required")
    val sections: List<CreateLessonSectionRequest> = emptyList(),
) {
    data class CreateLessonSectionRequest(
        @field:NotBlank(message = "Section title is required")
        val title: String,
        val type: SectionType,
        val data: Any? = null,
    )

    fun toInput(): CreateLessonInput {
        return CreateLessonInput(
            challengeId = challengeId,
            sections =
                sections.map {
                    CreateLessonInput.CreateLessonSectionInput(
                        title = it.title,
                        type = it.type,
                        data = it.data,
                    )
                },
        )
    }
}
