package lab.ujumeonji.moco.controller.challenge

import jakarta.validation.Valid
import lab.ujumeonji.moco.controller.challenge.dto.CreateLessonRequest
import lab.ujumeonji.moco.controller.challenge.dto.GetLessonsRequest
import lab.ujumeonji.moco.controller.challenge.dto.LessonResponse
import lab.ujumeonji.moco.model.challenge.LessonService
import lab.ujumeonji.moco.model.challenge.SectionType
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
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

        if (request.challengeId != null && request.sectionType != null) {
            try {
                val type = SectionType.valueOf(request.sectionType.uppercase())
                val outputPage = lessonService.findByChallengeIdAndSectionType(request.challengeId, type, pageable)
                val responsePage =
                    PageImpl(
                        outputPage.content.map { LessonResponse.from(it) },
                        outputPage.pageable,
                        outputPage.totalElements,
                    )
                return ResponseEntity.ok(responsePage)
            } catch (e: IllegalArgumentException) {
                return ResponseEntity.badRequest().build()
            }
        }

        if (request.challengeId != null) {
            val outputPage = lessonService.findByChallengeId(request.challengeId, pageable)
            val responsePage =
                PageImpl(
                    outputPage.content.map { LessonResponse.from(it) },
                    outputPage.pageable,
                    outputPage.totalElements,
                )
            return ResponseEntity.ok(responsePage)
        }

        if (request.sectionType != null) {
            try {
                val type = SectionType.valueOf(request.sectionType.uppercase())
                val outputPage = lessonService.findBySectionType(type, pageable)
                val responsePage =
                    PageImpl(
                        outputPage.content.map { LessonResponse.from(it) },
                        outputPage.pageable,
                        outputPage.totalElements,
                    )
                return ResponseEntity.ok(responsePage)
            } catch (e: IllegalArgumentException) {
                return ResponseEntity.badRequest().build()
            }
        }

        val outputPage = lessonService.findAll(pageable)
        val responsePage =
            PageImpl(
                outputPage.content.map { LessonResponse.from(it) },
                outputPage.pageable,
                outputPage.totalElements,
            )
        return ResponseEntity.ok(responsePage)
    }

    @PostMapping
    fun createLesson(
        @Valid @RequestBody request: CreateLessonRequest,
    ): ResponseEntity<LessonResponse> {
        return try {
            val output = lessonService.createLesson(request.toInput())
            ResponseEntity.status(HttpStatus.CREATED).body(LessonResponse.from(output))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        }
    }
}
