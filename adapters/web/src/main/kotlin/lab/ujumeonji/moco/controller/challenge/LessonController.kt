package lab.ujumeonji.moco.controller.challenge

import lab.ujumeonji.moco.model.Lesson
import lab.ujumeonji.moco.model.LessonSection
import lab.ujumeonji.moco.model.SectionType
import lab.ujumeonji.moco.service.challenge.LessonService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
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

    @GetMapping("/{id}")
    fun getLesson(
        @PathVariable id: String,
    ): ResponseEntity<Lesson> {
        val lesson = lessonService.findById(id)
        return if (lesson != null) {
            ResponseEntity.ok(lesson)
        } else {
            ResponseEntity.notFound().build()
        }
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

    @PostMapping("/templates")
    fun createLessonFromTemplate(
        @RequestParam templateType: String,
        @RequestParam challengeId: String,
    ): ResponseEntity<Lesson> {
        return try {
            val lesson =
                when (templateType.lowercase()) {
                    "binary-search" -> lessonService.createBinarySearchLesson(challengeId)
                    "tree-traversal" -> lessonService.createTreeTraversalLesson(challengeId)
                    else -> return ResponseEntity.badRequest().build()
                }
            ResponseEntity.status(HttpStatus.CREATED).body(lesson)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        }
    }

    @PutMapping("/{id}")
    fun updateLesson(
        @PathVariable id: String,
        @RequestBody lesson: Lesson,
    ): ResponseEntity<Lesson> {
        val updatedLesson = lessonService.update(id, lesson)
        return if (updatedLesson != null) {
            ResponseEntity.ok(updatedLesson)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PatchMapping("/{id}/sections")
    fun updateLessonSections(
        @PathVariable id: String,
        @RequestBody sections: List<LessonSection>,
    ): ResponseEntity<Lesson> {
        val updatedLesson = lessonService.updateSections(id, sections)
        return if (updatedLesson != null) {
            ResponseEntity.ok(updatedLesson)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/{id}")
    fun deleteLesson(
        @PathVariable id: String,
    ): ResponseEntity<Void> {
        lessonService.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}
