package lab.ujumeonji.moco.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "lessons")
data class LessonEntity(
    @Id
    val id: String? = null,
    val challengeId: String,
    val sections: List<LessonSectionEntity> = emptyList(),
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
)

data class LessonSectionEntity(
    val title: String,
    val type: SectionTypeEntity,
    val data: Any? = null,
)

enum class SectionTypeEntity {
    GAP_FILL,
    IMPLEMENTATION,
    TEXT,
}

data class QuizEntity(
    val question: String,
    val displayTokens: List<TokenEntity>,
    val choices: List<QuizOptionEntity>,
    val correctOptionIndex: Int,
)

data class TokenEntity(
    val text: String,
    val isBlank: Boolean,
)

data class QuizOptionEntity(
    val text: String,
    val isCorrect: Boolean,
)

data class CodeExerciseEntity(
    val title: String,
    val description: String,
    val codeSteps: List<CodeStepEntity>,
)

data class CodeStepEntity(
    val order: Int,
    val code: String,
    val explanation: String,
)
