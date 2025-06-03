package lab.ujumeonji.moco.controller.challenge

import jakarta.validation.Valid
import lab.ujumeonji.moco.controller.challenge.dto.CreateLessonRequest
import lab.ujumeonji.moco.controller.challenge.dto.LessonResponse
import lab.ujumeonji.moco.model.challenge.LessonService
import lab.ujumeonji.moco.model.challenge.SectionType
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/lessons")
class LessonController(private val lessonService: LessonService) {
    @GetMapping
    fun getLessons(
        @RequestParam(required = false) challengeId: String?,
        @RequestParam(required = false) sectionType: String?,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "createdAt,desc") sort: String,
    ): ResponseEntity<Page<LessonResponse>> {
        val sortParams = sort.split(",")
        val direction =
            if (sortParams.size > 1 && sortParams[1].equals("asc", ignoreCase = true)) {
                Sort.Direction.ASC
            } else {
                Sort.Direction.DESC
            }
        val sortProperty = sortParams[0]
        val pageable: Pageable = PageRequest.of(page, size, Sort.by(direction, sortProperty))

        if (challengeId != null && sectionType != null) {
            try {
                val type = SectionType.valueOf(sectionType.uppercase())
                val outputPage = lessonService.findByChallengeIdAndSectionTypeOutput(challengeId, type, pageable)
                val responsePage = PageImpl(
                    outputPage.content.map { LessonResponse.from(it) },
                    outputPage.pageable,
                    outputPage.totalElements
                )
                return ResponseEntity.ok(responsePage)
            } catch (e: IllegalArgumentException) {
                return ResponseEntity.badRequest().build()
            }
        }

        if (challengeId != null) {
            val outputPage = lessonService.findByChallengeIdOutput(challengeId, pageable)
            val responsePage = PageImpl(
                outputPage.content.map { LessonResponse.from(it) },
                outputPage.pageable,
                outputPage.totalElements
            )
            return ResponseEntity.ok(responsePage)
        }

        if (sectionType != null) {
            try {
                val type = SectionType.valueOf(sectionType.uppercase())
                val outputPage = lessonService.findBySectionTypeOutput(type, pageable)
                val responsePage = PageImpl(
                    outputPage.content.map { LessonResponse.from(it) },
                    outputPage.pageable,
                    outputPage.totalElements
                )
                return ResponseEntity.ok(responsePage)
            } catch (e: IllegalArgumentException) {
                return ResponseEntity.badRequest().build()
            }
        }

        val outputPage = lessonService.findAllOutput(pageable)
        val responsePage = PageImpl(
            outputPage.content.map { LessonResponse.from(it) },
            outputPage.pageable,
            outputPage.totalElements
        )
        return ResponseEntity.ok(responsePage)
    }

    @PostMapping
    fun createLesson(
        @Valid @RequestBody request: CreateLessonRequest,
    ): ResponseEntity<LessonResponse> {
        return try {
            val output = lessonService.saveOutput(request.toInput())
            ResponseEntity.status(HttpStatus.CREATED).body(LessonResponse.from(output))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        }
    }
}
