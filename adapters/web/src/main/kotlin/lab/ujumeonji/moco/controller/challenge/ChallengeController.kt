package lab.ujumeonji.moco.controller.challenge

import lab.ujumeonji.moco.model.challenge.Challenge
import lab.ujumeonji.moco.model.challenge.ChallengeService
import lab.ujumeonji.moco.model.challenge.ChatService
import lab.ujumeonji.moco.service.challenge.io.ChallengeChatInput
import lab.ujumeonji.moco.service.challenge.io.ChallengeChatOutput
import lab.ujumeonji.moco.support.auth.RequiredAuth
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
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
    ): ResponseEntity<Page<Challenge>> {
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
            val challenge = challengeService.findByTitle(title)
            return if (challenge != null) {
                val singleItemPage = PageImpl(listOf(challenge), pageable, 1)
                ResponseEntity.ok(singleItemPage)
            } else {
                ResponseEntity.ok(PageImpl(emptyList<Challenge>(), pageable, 0))
            }
        }

        if (difficulty != null) {
            return ResponseEntity.ok(challengeService.findByDifficulty(difficulty, pageable))
        }

        if (tag != null) {
            return ResponseEntity.ok(challengeService.findByTag(tag, pageable))
        }

        return ResponseEntity.ok(challengeService.findAll(pageable))
    }

    @GetMapping("/{id}")
    fun getChallenge(
        @PathVariable id: String,
    ): ResponseEntity<Challenge> {
        val challenge = challengeService.findById(id)
        return if (challenge != null) {
            ResponseEntity.ok(challenge)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping
    fun createChallenge(
        @RequestBody challenge: Challenge,
    ): ResponseEntity<Challenge> {
        return ResponseEntity.status(HttpStatus.CREATED).body(challengeService.save(challenge))
    }

    @PutMapping("/{id}")
    fun updateChallenge(
        @PathVariable id: String,
        @RequestBody challenge: Challenge,
    ): ResponseEntity<Challenge> {
        val updatedChallenge = challengeService.update(id, challenge)
        return if (updatedChallenge != null) {
            ResponseEntity.ok(updatedChallenge)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/{id}")
    fun deleteChallenge(
        @PathVariable id: String,
    ): ResponseEntity<Void> {
        challengeService.deleteById(id)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/{challengeId}/chats")
    fun chatAboutChallenge(
        @RequiredAuth userId: String,
        @PathVariable challengeId: String,
        @RequestBody request: ChallengeChatInput,
    ): ResponseEntity<ChallengeChatOutput> {
        try {
            val response = chatService.processChat(challengeId, userId, request)
            return ResponseEntity.ok(response)
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
    ): ResponseEntity<ChallengeChatOutput> {
        try {
            val chatSessions = chatService.getChatSessionsByChallengeAndUser(challengeId, userId)
            return ResponseEntity.ok(chatSessions)
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.badRequest().build()
        } catch (e: Exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }
}
