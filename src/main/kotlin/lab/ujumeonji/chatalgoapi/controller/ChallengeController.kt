package lab.ujumeonji.chatalgoapi.controller

import lab.ujumeonji.chatalgoapi.model.Challenge
import lab.ujumeonji.chatalgoapi.service.ChallengeService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/challenges")
class ChallengeController(private val challengeService: ChallengeService) {
    
    /**
     * 모든 챌린지를 조회합니다.
     */
    @GetMapping
    fun getAllChallenges(): ResponseEntity<List<Challenge>> {
        return ResponseEntity.ok(challengeService.findAll())
    }
    
    /**
     * ID로 특정 챌린지를 조회합니다.
     */
    @GetMapping("/{id}")
    fun getChallengeById(@PathVariable id: String): ResponseEntity<Challenge> {
        val challenge = challengeService.findById(id)
        return if (challenge != null) {
            ResponseEntity.ok(challenge)
        } else {
            ResponseEntity.notFound().build()
        }
    }
    
    /**
     * 제목으로 특정 챌린지를 조회합니다.
     */
    @GetMapping("/title/{title}")
    fun getChallengeByTitle(@PathVariable title: String): ResponseEntity<Challenge> {
        val challenge = challengeService.findByTitle(title)
        return if (challenge != null) {
            ResponseEntity.ok(challenge)
        } else {
            ResponseEntity.notFound().build()
        }
    }
    
    /**
     * 난이도별로 챌린지를 조회합니다.
     */
    @GetMapping("/difficulty/{difficulty}")
    fun getChallengesByDifficulty(@PathVariable difficulty: String): ResponseEntity<List<Challenge>> {
        return ResponseEntity.ok(challengeService.findByDifficulty(difficulty))
    }
    
    /**
     * 태그별로 챌린지를 조회합니다.
     */
    @GetMapping("/tag/{tag}")
    fun getChallengesByTag(@PathVariable tag: String): ResponseEntity<List<Challenge>> {
        return ResponseEntity.ok(challengeService.findByTag(tag))
    }
    
    /**
     * 새로운 챌린지를 생성합니다.
     */
    @PostMapping
    fun createChallenge(@RequestBody challenge: Challenge): ResponseEntity<Challenge> {
        return ResponseEntity.status(HttpStatus.CREATED).body(challengeService.save(challenge))
    }
    
    /**
     * 기존 챌린지를 수정합니다.
     */
    @PutMapping("/{id}")
    fun updateChallenge(
        @PathVariable id: String,
        @RequestBody challenge: Challenge
    ): ResponseEntity<Challenge> {
        val updatedChallenge = challengeService.update(id, challenge)
        return if (updatedChallenge != null) {
            ResponseEntity.ok(updatedChallenge)
        } else {
            ResponseEntity.notFound().build()
        }
    }
    
    /**
     * 특정 챌린지를 삭제합니다.
     */
    @DeleteMapping("/{id}")
    fun deleteChallenge(@PathVariable id: String): ResponseEntity<Void> {
        challengeService.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}
