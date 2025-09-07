package lab.ujumeonji.moco.controller.challenge

import jakarta.validation.Valid
import lab.ujumeonji.moco.controller.challenge.dto.CreateLessonRequest
import lab.ujumeonji.moco.controller.challenge.dto.GetLessonsRequest
import lab.ujumeonji.moco.controller.challenge.dto.LessonResponse
import lab.ujumeonji.moco.model.challenge.LessonService
import lab.ujumeonji.moco.support.auth.RequiredAuth
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/lessons")
class LessonController(private val lessonService: LessonService) {
    @GetMapping
    fun getLessons(
        @ModelAttribute @Valid request: GetLessonsRequest,
    ): ResponseEntity<Page<LessonResponse>> {
        val pageable = request.toPageable()
        val outputPage =
            lessonService.searchLessons(
                challengeId = request.challengeId,
                sectionType = request.sectionType,
                pageable = pageable,
            )

        val responsePage = outputPage.map { LessonResponse.from(it) }
        return ResponseEntity.ok(responsePage)
    }

    @PostMapping
    fun createLesson(
        @RequiredAuth userId: String,
        @Valid @RequestBody request: CreateLessonRequest,
    ): ResponseEntity<LessonResponse> {
        val output = lessonService.createLesson(request.toInput())
        return ResponseEntity.status(HttpStatus.CREATED).body(LessonResponse.from(output))
    }
}
