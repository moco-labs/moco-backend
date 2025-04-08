package lab.ujumeonji.chatalgoapi.controller

import lab.ujumeonji.chatalgoapi.model.Lesson
import lab.ujumeonji.chatalgoapi.model.LessonSection
import lab.ujumeonji.chatalgoapi.model.SectionType
import lab.ujumeonji.chatalgoapi.service.LessonService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/lessons")
class LessonController(private val lessonService: LessonService) {

    /**
     * 모든 레슨을 조회합니다.
     */
    @GetMapping
    fun getLessons(
        @RequestParam(required = false) challengeId: String?,
        @RequestParam(required = false) sectionType: String?
    ): ResponseEntity<List<Lesson>> {
        // 챌린지 ID와 섹션 타입 모두 제공된 경우
        if (challengeId != null && sectionType != null) {
            try {
                val type = SectionType.valueOf(sectionType.uppercase())
                return ResponseEntity.ok(lessonService.findByChallengeIdAndSectionType(challengeId, type))
            } catch (e: IllegalArgumentException) {
                return ResponseEntity.badRequest().build()
            }
        }
        
        // 챌린지 ID만 제공된 경우
        if (challengeId != null) {
            return ResponseEntity.ok(lessonService.findByChallengeId(challengeId))
        }
        
        // 섹션 타입만 제공된 경우
        if (sectionType != null) {
            try {
                val type = SectionType.valueOf(sectionType.uppercase())
                return ResponseEntity.ok(lessonService.findBySectionType(type))
            } catch (e: IllegalArgumentException) {
                return ResponseEntity.badRequest().build()
            }
        }
        
        // 파라미터가 없는 경우 모든 레슨 반환
        return ResponseEntity.ok(lessonService.findAll())
    }

    /**
     * ID로 특정 레슨을 조회합니다.
     */
    @GetMapping("/{id}")
    fun getLesson(@PathVariable id: String): ResponseEntity<Lesson> {
        val lesson = lessonService.findById(id)
        return if (lesson != null) {
            ResponseEntity.ok(lesson)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    /**
     * 새로운 레슨을 생성합니다.
     */
    @PostMapping
    fun createLesson(@RequestBody lesson: Lesson): ResponseEntity<Lesson> {
        return try {
            ResponseEntity.status(HttpStatus.CREATED).body(lessonService.save(lesson))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        }
    }

    /**
     * 템플릿 기반 레슨을 생성합니다.
     */
    @PostMapping("/templates")
    fun createLessonFromTemplate(
        @RequestParam templateType: String,
        @RequestParam challengeId: String
    ): ResponseEntity<Lesson> {
        return try {
            val lesson = when (templateType.lowercase()) {
                "binary-search" -> lessonService.createBinarySearchLesson(challengeId)
                "tree-traversal" -> lessonService.createTreeTraversalLesson(challengeId)
                else -> return ResponseEntity.badRequest().build()
            }
            ResponseEntity.status(HttpStatus.CREATED).body(lesson)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        }
    }

    /**
     * 기존 레슨을 수정합니다.
     */
    @PutMapping("/{id}")
    fun updateLesson(
        @PathVariable id: String,
        @RequestBody lesson: Lesson
    ): ResponseEntity<Lesson> {
        val updatedLesson = lessonService.update(id, lesson)
        return if (updatedLesson != null) {
            ResponseEntity.ok(updatedLesson)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    /**
     * 레슨의 섹션을 업데이트합니다.
     */
    @PatchMapping("/{id}/sections")
    fun updateLessonSections(
        @PathVariable id: String,
        @RequestBody sections: List<LessonSection>
    ): ResponseEntity<Lesson> {
        val updatedLesson = lessonService.updateSections(id, sections)
        return if (updatedLesson != null) {
            ResponseEntity.ok(updatedLesson)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    /**
     * 특정 레슨을 삭제합니다.
     */
    @DeleteMapping("/{id}")
    fun deleteLesson(@PathVariable id: String): ResponseEntity<Void> {
        lessonService.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}
