package lab.ujumeonji.moco.controller.challenge

import lab.ujumeonji.moco.model.Challenge
import lab.ujumeonji.moco.model.DailyChallenge
import lab.ujumeonji.moco.model.challenge.DailyChallengeService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/api/daily-challenges")
class DailyChallengeController(private val dailyChallengeService: DailyChallengeService) {
    @GetMapping
    fun getDailyChallenges(
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) startDate: LocalDate?,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) endDate: LocalDate?,
    ): ResponseEntity<List<DailyChallenge>> {
        return if (startDate != null && endDate != null) {
            ResponseEntity.ok(dailyChallengeService.findByDateRange(startDate, endDate))
        } else {
            ResponseEntity.ok(dailyChallengeService.findAll())
        }
    }

    @GetMapping("/{id}")
    fun getDailyChallenge(
        @PathVariable id: String,
    ): ResponseEntity<DailyChallenge> {
        val dailyChallenge = dailyChallengeService.findById(id)
        return if (dailyChallenge != null) {
            ResponseEntity.ok(dailyChallenge)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/today")
    fun getTodayChallenge(): ResponseEntity<Challenge> {
        val challenge = dailyChallengeService.findTodayChallenge()
        return if (challenge != null) {
            ResponseEntity.ok(challenge)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/date/{date}")
    fun getDailyChallengeByDate(
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) date: LocalDate,
    ): ResponseEntity<Challenge> {
        val challenge = dailyChallengeService.findChallengeForDate(date)
        return if (challenge != null) {
            ResponseEntity.ok(challenge)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping
    fun createDailyChallenge(
        @RequestBody dailyChallenge: DailyChallenge,
    ): ResponseEntity<DailyChallenge> {
        return try {
            ResponseEntity.status(HttpStatus.CREATED).body(dailyChallengeService.save(dailyChallenge))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        }
    }

    @PostMapping("/set")
    fun setDailyChallenge(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) date: LocalDate,
        @RequestParam challengeId: String,
    ): ResponseEntity<DailyChallenge> {
        return try {
            ResponseEntity.status(HttpStatus.CREATED).body(dailyChallengeService.setDailyChallenge(date, challengeId))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        }
    }

    @PostMapping("/random")
    fun setRandomDailyChallenge(
        @RequestParam(required = false, defaultValue = "7") excludeRecentDays: Int,
    ): ResponseEntity<DailyChallenge> {
        val dailyChallenge = dailyChallengeService.setRandomDailyChallenge(excludeRecentDays)
        return if (dailyChallenge != null) {
            ResponseEntity.status(HttpStatus.CREATED).body(dailyChallenge)
        } else {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    @PutMapping("/{id}")
    fun updateDailyChallenge(
        @PathVariable id: String,
        @RequestBody dailyChallenge: DailyChallenge,
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

    @DeleteMapping("/{id}")
    fun deleteDailyChallenge(
        @PathVariable id: String,
    ): ResponseEntity<Void> {
        dailyChallengeService.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}
