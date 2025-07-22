package lab.ujumeonji.moco.controller.challenge

import jakarta.validation.Valid
import lab.ujumeonji.moco.controller.challenge.dto.ChallengeChatRequest
import lab.ujumeonji.moco.controller.challenge.dto.ChallengeChatResponse
import lab.ujumeonji.moco.controller.challenge.dto.ChallengeResponse
import lab.ujumeonji.moco.controller.challenge.dto.CreateChallengeRequest
import lab.ujumeonji.moco.controller.challenge.dto.GetChallengesRequest
import lab.ujumeonji.moco.model.challenge.ChallengeService
import lab.ujumeonji.moco.model.challenge.ChatService
import lab.ujumeonji.moco.support.auth.RequiredAuth
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
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/challenges")
class ChallengeController(
    private val challengeService: ChallengeService,
    private val chatService: ChatService,
) {
    @GetMapping
    fun getChallenges(
        @ModelAttribute @Valid request: GetChallengesRequest,
    ): ResponseEntity<Page<ChallengeResponse>> {
        val pageable = request.toPageable()

        if (request.title != null) {
            val challengeOutput = challengeService.findByTitle(request.title)
            return if (challengeOutput != null) {
                val challengeResponse = ChallengeResponse.from(challengeOutput)
                val singleItemPage = PageImpl(listOf(challengeResponse), pageable, 1)
                ResponseEntity.ok(singleItemPage)
            } else {
                ResponseEntity.ok(PageImpl(emptyList<ChallengeResponse>(), pageable, 0))
            }
        }

        if (request.difficulty != null) {
            val outputPage = challengeService.findByDifficulty(request.difficulty, pageable)
            val responsePage =
                PageImpl(
                    outputPage.content.map { ChallengeResponse.from(it) },
                    outputPage.pageable,
                    outputPage.totalElements,
                )
            return ResponseEntity.ok(responsePage)
        }

        if (request.tag != null) {
            val outputPage = challengeService.findByTag(request.tag, pageable)
            val responsePage =
                PageImpl(
                    outputPage.content.map { ChallengeResponse.from(it) },
                    outputPage.pageable,
                    outputPage.totalElements,
                )
            return ResponseEntity.ok(responsePage)
        }

        val outputPage = challengeService.findAll(pageable)
        val responsePage =
            PageImpl(
                outputPage.content.map { ChallengeResponse.from(it) },
                outputPage.pageable,
                outputPage.totalElements,
            )
        return ResponseEntity.ok(responsePage)
    }

    @GetMapping("/{id}")
    fun getChallenge(
        @PathVariable id: String,
    ): ResponseEntity<ChallengeResponse> {
        val challengeOutput = challengeService.findById(id)
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
        val challengeOutput = challengeService.create(request.toInput())
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
