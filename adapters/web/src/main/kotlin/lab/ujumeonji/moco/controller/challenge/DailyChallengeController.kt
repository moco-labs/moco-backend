package lab.ujumeonji.moco.controller.challenge

import jakarta.validation.Valid
import lab.ujumeonji.moco.controller.challenge.dto.CreateDailyChallengeRequest
import lab.ujumeonji.moco.controller.challenge.dto.DailyChallengeResponse
import lab.ujumeonji.moco.model.challenge.DailyChallengeService
import lab.ujumeonji.moco.model.challenge.io.DailyChallengeOutput
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
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
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "date,desc") sort: String,
    ): ResponseEntity<Page<DailyChallengeResponse>> {
        val sortParams = sort.split(",")
        val direction =
            if (sortParams.size > 1 && sortParams[1].equals("asc", ignoreCase = true)) {
                Sort.Direction.ASC
            } else {
                Sort.Direction.DESC
            }
        val sortProperty = sortParams[0]
        val pageable: Pageable = PageRequest.of(page, size, Sort.by(direction, sortProperty))

        if (startDate != null && endDate != null) {
            val outputPage = dailyChallengeService.findByDateRangeOutput(startDate, endDate, pageable)
            val responsePage = PageImpl(
                outputPage.content.map { DailyChallengeResponse.from(it) },
                outputPage.pageable,
                outputPage.totalElements
            )
            return ResponseEntity.ok(responsePage)
        } else {
            val outputPage = dailyChallengeService.findAllOutput(pageable)
            val responsePage = PageImpl(
                outputPage.content.map { DailyChallengeResponse.from(it) },
                outputPage.pageable,
                outputPage.totalElements
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
