package lab.ujumeonji.moco.controller.challenge

import jakarta.validation.Valid
import lab.ujumeonji.moco.controller.challenge.dto.ChallengeChatRequest
import lab.ujumeonji.moco.controller.challenge.dto.ChallengeChatResponse
import lab.ujumeonji.moco.controller.challenge.dto.ChallengeResponse
import lab.ujumeonji.moco.controller.challenge.dto.CreateChallengeRequest
import lab.ujumeonji.moco.model.challenge.ChallengeService
import lab.ujumeonji.moco.model.challenge.ChatService
import lab.ujumeonji.moco.service.challenge.io.ChallengeChatInput
import lab.ujumeonji.moco.service.challenge.io.ChallengeChatOutput
import lab.ujumeonji.moco.service.challenge.io.ChallengeOutput
import lab.ujumeonji.moco.support.auth.RequiredAuth
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/challenges")
class ChallengeController(
    private val challengeService: ChallengeService,
    private val chatService: ChatService,
) {
    @GetMapping
    fun getChallenges(
        @RequestParam(required = false) title: String?,
        @RequestParam(required = false) difficulty: String?,
        @RequestParam(required = false) tag: String?,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "createdAt,desc") sort: String,
    ): ResponseEntity<Page<ChallengeResponse>> {
        val sortParams = sort.split(",")
        val direction =
            if (sortParams.size > 1 && sortParams[1].equals("asc", ignoreCase = true)) {
                Sort.Direction.ASC
            } else {
                Sort.Direction.DESC
            }
        val sortProperty = sortParams[0]
        val pageable: Pageable = PageRequest.of(page, size, Sort.by(direction, sortProperty))

        if (title != null) {
            val challengeOutput = challengeService.findByTitleOutput(title)
            return if (challengeOutput != null) {
                val challengeResponse = ChallengeResponse.from(challengeOutput)
                val singleItemPage = PageImpl(listOf(challengeResponse), pageable, 1)
                ResponseEntity.ok(singleItemPage)
            } else {
                ResponseEntity.ok(PageImpl(emptyList<ChallengeResponse>(), pageable, 0))
            }
        }

        if (difficulty != null) {
            val outputPage = challengeService.findByDifficultyOutput(difficulty, pageable)
            val responsePage = PageImpl(
                outputPage.content.map { ChallengeResponse.from(it) },
                outputPage.pageable,
                outputPage.totalElements
            )
            return ResponseEntity.ok(responsePage)
        }

        if (tag != null) {
            val outputPage = challengeService.findByTagOutput(tag, pageable)
            val responsePage = PageImpl(
                outputPage.content.map { ChallengeResponse.from(it) },
                outputPage.pageable,
                outputPage.totalElements
            )
            return ResponseEntity.ok(responsePage)
        }

        val outputPage = challengeService.findAllOutput(pageable)
        val responsePage = PageImpl(
            outputPage.content.map { ChallengeResponse.from(it) },
            outputPage.pageable,
            outputPage.totalElements
        )
        return ResponseEntity.ok(responsePage)
    }

    @GetMapping("/{id}")
    fun getChallenge(
        @PathVariable id: String,
    ): ResponseEntity<ChallengeResponse> {
        val challengeOutput = challengeService.findByIdOutput(id)
        return if (challengeOutput != null) {
            ResponseEntity.ok(ChallengeResponse.from(challengeOutput))
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping
    fun createChallenge(
        @Valid @RequestBody request: CreateChallengeRequest,
    ): ResponseEntity<ChallengeResponse> {
        val challengeOutput = challengeService.saveOutput(request.toInput())
        return ResponseEntity.status(HttpStatus.CREATED).body(ChallengeResponse.from(challengeOutput))
    }

    @PostMapping("/{challengeId}/chats")
    fun chatAboutChallenge(
        @RequiredAuth userId: String,
        @PathVariable challengeId: String,
        @Valid @RequestBody request: ChallengeChatRequest,
    ): ResponseEntity<ChallengeChatResponse> {
        try {
            val response = chatService.processChat(challengeId, userId, request.toInput())
            return ResponseEntity.ok(ChallengeChatResponse.from(response))
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.badRequest().build()
        } catch (e: Exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    @GetMapping("/{challengeId}/chats")
    fun getChatSessionsByChallenge(
        @RequiredAuth userId: String,
        @PathVariable challengeId: String,
    ): ResponseEntity<ChallengeChatResponse> {
        try {
            val chatSessions = chatService.getChatSessionsByChallengeAndUser(challengeId, userId)
            return ResponseEntity.ok(ChallengeChatResponse.from(chatSessions))
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.badRequest().build()
        } catch (e: Exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }
}
