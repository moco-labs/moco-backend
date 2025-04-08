package lab.ujumeonji.chatalgoapi.controller

import lab.ujumeonji.chatalgoapi.model.Challenge
import lab.ujumeonji.chatalgoapi.model.DailyChallenge
import lab.ujumeonji.chatalgoapi.service.DailyChallengeService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/api/daily-challenges")
class DailyChallengeController(private val dailyChallengeService: DailyChallengeService) {

    /**
     * 모든 일일 챌린지를 조회하거나 날짜 범위로 필터링합니다.
     */
    @GetMapping
    fun getDailyChallenges(
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) startDate: LocalDate?,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) endDate: LocalDate?
    ): ResponseEntity<List<DailyChallenge>> {
        return if (startDate != null && endDate != null) {
            ResponseEntity.ok(dailyChallengeService.findByDateRange(startDate, endDate))
        } else {
            ResponseEntity.ok(dailyChallengeService.findAll())
        }
    }

    /**
     * ID로 특정 일일 챌린지를 조회합니다.
     */
    @GetMapping("/{id}")
    fun getDailyChallenge(@PathVariable id: String): ResponseEntity<DailyChallenge> {
        val dailyChallenge = dailyChallengeService.findById(id)
        return if (dailyChallenge != null) {
            ResponseEntity.ok(dailyChallenge)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    /**
     * 오늘의 일일 챌린지를 조회합니다.
     */
    @GetMapping("/today")
    fun getTodayChallenge(): ResponseEntity<Challenge> {
        val challenge = dailyChallengeService.findTodayChallenge()
        return if (challenge != null) {
            ResponseEntity.ok(challenge)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    /**
     * 특정 날짜의 일일 챌린지를 조회합니다.
     */
    @GetMapping("/date/{date}")
    fun getDailyChallengeByDate(
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) date: LocalDate
    ): ResponseEntity<Challenge> {
        val challenge = dailyChallengeService.findChallengeForDate(date)
        return if (challenge != null) {
            ResponseEntity.ok(challenge)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    /**
     * 새로운 일일 챌린지를 생성합니다.
     */
    @PostMapping
    fun createDailyChallenge(@RequestBody dailyChallenge: DailyChallenge): ResponseEntity<DailyChallenge> {
        return try {
            ResponseEntity.status(HttpStatus.CREATED).body(dailyChallengeService.save(dailyChallenge))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        }
    }

    /**
     * 특정 날짜에 일일 챌린지를 설정합니다.
     */
    @PostMapping("/set")
    fun setDailyChallenge(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) date: LocalDate,
        @RequestParam challengeId: String
    ): ResponseEntity<DailyChallenge> {
        return try {
            ResponseEntity.status(HttpStatus.CREATED).body(dailyChallengeService.setDailyChallenge(date, challengeId))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        }
    }
    
    /**
     * 랜덤 챌린지를 오늘의 일일 챌린지로 설정합니다.
     */
    @PostMapping("/random")
    fun setRandomDailyChallenge(
        @RequestParam(required = false, defaultValue = "7") excludeRecentDays: Int
    ): ResponseEntity<DailyChallenge> {
        val dailyChallenge = dailyChallengeService.setRandomDailyChallenge(excludeRecentDays)
        return if (dailyChallenge != null) {
            ResponseEntity.status(HttpStatus.CREATED).body(dailyChallenge)
        } else {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    /**
     * 일일 챌린지를 업데이트합니다.
     */
    @PutMapping("/{id}")
    fun updateDailyChallenge(
        @PathVariable id: String,
        @RequestBody dailyChallenge: DailyChallenge
    ): ResponseEntity<DailyChallenge> {
        return try {
            val updatedDailyChallenge = dailyChallengeService.update(id, dailyChallenge)
            if (updatedDailyChallenge != null) {
                ResponseEntity.ok(updatedDailyChallenge)
            } else {
                ResponseEntity.notFound().build()
            }
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        }
    }

    /**
     * 일일 챌린지를 삭제합니다.
     */
    @DeleteMapping("/{id}")
    fun deleteDailyChallenge(@PathVariable id: String): ResponseEntity<Void> {
        dailyChallengeService.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}
