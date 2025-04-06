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
    fun getAllLessons(): ResponseEntity<List<Lesson>> {
        return ResponseEntity.ok(lessonService.findAll())
    }
    
    /**
     * ID로 특정 레슨을 조회합니다.
     */
    @GetMapping("/{id}")
    fun getLessonById(@PathVariable id: String): ResponseEntity<Lesson> {
        val lesson = lessonService.findById(id)
        return if (lesson != null) {
            ResponseEntity.ok(lesson)
        } else {
            ResponseEntity.notFound().build()
        }
    }
    
    /**
     * 챌린지 ID로 레슨을 조회합니다.
     */
    @GetMapping("/challenge/{challengeId}")
    fun getLessonsByChallengeId(@PathVariable challengeId: String): ResponseEntity<List<Lesson>> {
        return ResponseEntity.ok(lessonService.findByChallengeId(challengeId))
    }
    
    /**
     * 섹션 유형으로 레슨을 조회합니다.
     */
    @GetMapping("/type/{type}")
    fun getLessonsBySectionType(@PathVariable type: String): ResponseEntity<List<Lesson>> {
        try {
            val sectionType = SectionType.valueOf(type.uppercase())
            return ResponseEntity.ok(lessonService.findBySectionType(sectionType))
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.badRequest().build()
        }
    }
    
    /**
     * 챌린지 ID와 섹션 유형으로 레슨을 조회합니다.
     */
    @GetMapping("/challenge/{challengeId}/type/{type}")
    fun getLessonsByChallengeIdAndSectionType(
        @PathVariable challengeId: String,
        @PathVariable type: String
    ): ResponseEntity<List<Lesson>> {
        try {
            val sectionType = SectionType.valueOf(type.uppercase())
            return ResponseEntity.ok(lessonService.findByChallengeIdAndSectionType(challengeId, sectionType))
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.badRequest().build()
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
     * 이진 검색 샘플 레슨을 생성합니다.
     */
    @PostMapping("/sample/binary-search/{challengeId}")
    fun createBinarySearchLesson(@PathVariable challengeId: String): ResponseEntity<Lesson> {
        return try {
            ResponseEntity.status(HttpStatus.CREATED).body(lessonService.createBinarySearchLesson(challengeId))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        }
    }
    
    /**
     * 트리 순회 샘플 레슨을 생성합니다.
     */
    @PostMapping("/sample/tree-traversal/{challengeId}")
    fun createTreeTraversalLesson(@PathVariable challengeId: String): ResponseEntity<Lesson> {
        return try {
            ResponseEntity.status(HttpStatus.CREATED).body(lessonService.createTreeTraversalLesson(challengeId))
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
    
    /**
     * 챌린지에 속한 모든 레슨을 삭제합니다.
     */
    @DeleteMapping("/challenge/{challengeId}")
    fun deleteLessonsByChallengeId(@PathVariable challengeId: String): ResponseEntity<Void> {
        lessonService.deleteByChallengeId(challengeId)
        return ResponseEntity.noContent().build()
    }
}
