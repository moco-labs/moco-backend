package lab.ujumeonji.moco.controller.challenge

import lab.ujumeonji.moco.model.challenge.Lesson
import lab.ujumeonji.moco.model.challenge.LessonService
import lab.ujumeonji.moco.model.challenge.SectionType
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
    ): ResponseEntity<List<Lesson>> {
        if (challengeId != null && sectionType != null) {
            try {
                val type = SectionType.valueOf(sectionType.uppercase())
                return ResponseEntity.ok(lessonService.findByChallengeIdAndSectionType(challengeId, type))
            } catch (e: IllegalArgumentException) {
                return ResponseEntity.badRequest().build()
            }
        }

        if (challengeId != null) {
            return ResponseEntity.ok(lessonService.findByChallengeId(challengeId))
        }

        if (sectionType != null) {
            try {
                val type = SectionType.valueOf(sectionType.uppercase())
                return ResponseEntity.ok(lessonService.findBySectionType(type))
            } catch (e: IllegalArgumentException) {
                return ResponseEntity.badRequest().build()
            }
        }

        return ResponseEntity.ok(lessonService.findAll())
    }

    @PostMapping
    fun createLesson(
        @RequestBody lesson: Lesson,
    ): ResponseEntity<Lesson> {
        return try {
            ResponseEntity.status(HttpStatus.CREATED).body(lessonService.save(lesson))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        }
    }
}
