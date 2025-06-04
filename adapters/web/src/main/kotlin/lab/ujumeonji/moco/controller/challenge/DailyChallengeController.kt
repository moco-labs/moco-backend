package lab.ujumeonji.moco.controller.challenge

import jakarta.validation.Valid
import lab.ujumeonji.moco.controller.challenge.dto.CreateDailyChallengeRequest
import lab.ujumeonji.moco.controller.challenge.dto.DailyChallengeResponse
import lab.ujumeonji.moco.controller.challenge.dto.GetDailyChallengesRequest
import lab.ujumeonji.moco.model.challenge.DailyChallengeService
import lab.ujumeonji.moco.model.challenge.io.DailyChallengeOutput
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/daily-challenges")
class DailyChallengeController(private val dailyChallengeService: DailyChallengeService) {
    @GetMapping
    fun getDailyChallenges(
        @ModelAttribute @Valid request: GetDailyChallengesRequest,
    ): ResponseEntity<Page<DailyChallengeResponse>> {
        val pageable = request.toPageable()

        if (request.startDate != null && request.endDate != null) {
            val outputPage = dailyChallengeService.findByDateRangeOutput(request.startDate, request.endDate, pageable)
            val responsePage =
                PageImpl(
                    outputPage.content.map { DailyChallengeResponse.from(it) },
                    outputPage.pageable,
                    outputPage.totalElements,
                )
            return ResponseEntity.ok(responsePage)
        } else {
            val outputPage = dailyChallengeService.findAllOutput(pageable)
            val responsePage =
                PageImpl(
                    outputPage.content.map { DailyChallengeResponse.from(it) },
                    outputPage.pageable,
                    outputPage.totalElements,
                )
            return ResponseEntity.ok(responsePage)
        }
    }

    @GetMapping("/{id}")
    fun getDailyChallenge(
        @PathVariable id: String,
    ): ResponseEntity<DailyChallengeResponse> {
        val dailyChallengeOutput = dailyChallengeService.findByIdOutput(id)
        return if (dailyChallengeOutput != null) {
            ResponseEntity.ok(DailyChallengeResponse.from(dailyChallengeOutput))
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping
    fun createDailyChallenge(
        @Valid @RequestBody request: CreateDailyChallengeRequest,
    ): ResponseEntity<DailyChallengeResponse> {
        return try {
            val output = dailyChallengeService.saveOutput(request.toInput())
            ResponseEntity.status(HttpStatus.CREATED).body(DailyChallengeResponse.from(output))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        }
    }

    @PostMapping("/random")
    fun setRandomDailyChallenge(
        @RequestParam(required = false, defaultValue = "7") excludeRecentDays: Int,
    ): ResponseEntity<DailyChallengeResponse> {
        val dailyChallenge = dailyChallengeService.setRandomDailyChallenge(excludeRecentDays)
        return if (dailyChallenge != null) {
            val output = DailyChallengeOutput.fromDomain(dailyChallenge)
            ResponseEntity.status(HttpStatus.CREATED).body(DailyChallengeResponse.from(output))
        } else {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }
}
