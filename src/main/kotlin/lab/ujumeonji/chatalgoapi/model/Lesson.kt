package lab.ujumeonji.chatalgoapi.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "lessons")
@TypeAlias("Lesson")
data class Lesson(
    @Id
    val id: String? = null,
    val challengeId: String,
    val sections: List<LessonSection> = emptyList(),
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
)

data class LessonSection(
    val title: String,
    val type: SectionType,
    val data: Any? = null,
)

enum class SectionType {
    GAP_FILL,
    IMPLEMENTATION,
    TEXT,
}

data class Quiz(
    val question: String,
    val displayTokens: List<Token>,
    val choices: List<QuizOption>,
    val correctOptionIndex: Int,
)

data class Token(
    val text: String,
    val isBlank: Boolean,
)

data class QuizOption(
    val text: String,
    val isCorrect: Boolean,
)

data class CodeExercise(
    val title: String,
    val description: String,
    val codeSteps: List<CodeStep>,
)

data class CodeStep(
    val order: Int,
    val code: String,
    val explanation: String,
)
