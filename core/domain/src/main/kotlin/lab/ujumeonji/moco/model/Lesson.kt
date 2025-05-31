package lab.ujumeonji.moco.model

import java.time.LocalDateTime

class Lesson(
    val id: String? = null,
    val challengeId: String,
    val sections: List<LessonSection> = emptyList(),
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
)

class LessonSection(
    val title: String,
    val type: SectionType,
    val data: Any? = null,
)

enum class SectionType {
    GAP_FILL,
    IMPLEMENTATION,
    TEXT,
}

class Quiz(
    val question: String,
    val displayTokens: List<Token>,
    val choices: List<QuizOption>,
    val correctOptionIndex: Int,
)

class Token(
    val text: String,
    val isBlank: Boolean,
)

class QuizOption(
    val text: String,
    val isCorrect: Boolean,
)

class CodeExercise(
    val title: String,
    val description: String,
    val codeSteps: List<CodeStep>,
)

class CodeStep(
    val order: Int,
    val code: String,
    val explanation: String,
)
