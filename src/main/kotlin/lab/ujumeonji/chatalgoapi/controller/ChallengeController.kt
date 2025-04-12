package lab.ujumeonji.chatalgoapi.controller

import lab.ujumeonji.chatalgoapi.dto.ChallengeChatRequest
import lab.ujumeonji.chatalgoapi.dto.ChallengeChatResponse
import lab.ujumeonji.chatalgoapi.model.Challenge
import lab.ujumeonji.chatalgoapi.service.ChallengeService
import lab.ujumeonji.chatalgoapi.service.ChatService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/challenges")
class ChallengeController(
    private val challengeService: ChallengeService,
    private val chatService: ChatService,
) {

    /**
     * 필터링 조건에 따른 챌린지 조회합니다.
     *
     * @param title 제목으로 필터링
     * @param difficulty 난이도로 필터링
     * @param tag 태그로 필터링
     * @return 필터링된: 챌린지 목록
     */
    @GetMapping
    fun getChallenges(
        @RequestParam(required = false) title: String?,
        @RequestParam(required = false) difficulty: String?,
        @RequestParam(required = false) tag: String?
    ): ResponseEntity<List<Challenge>> {
        // 제목으로 필터링
        if (title != null) {
            val challenge = challengeService.findByTitle(title)
            return if (challenge != null) {
                ResponseEntity.ok(listOf(challenge))
            } else {
                ResponseEntity.ok(emptyList())
            }
        }

        // 난이도로 필터링
        if (difficulty != null) {
            return ResponseEntity.ok(challengeService.findByDifficulty(difficulty))
        }

        // 태그로 필터링
        if (tag != null) {
            return ResponseEntity.ok(challengeService.findByTag(tag))
        }

        // 필터링 조건이 없으면 모든 챌린지 반환
        return ResponseEntity.ok(challengeService.findAll())
    }

    /**
     * ID로 특정 챌린지를 조회합니다.
     */
    @GetMapping("/{id}")
    fun getChallenge(@PathVariable id: String): ResponseEntity<Challenge> {
        val challenge = challengeService.findById(id)
        return if (challenge != null) {
            ResponseEntity.ok(challenge)
        } else {
            ResponseEntity.notFound().build()
        }
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

    /**
     * 특정 챌린지에 대한 대화형 학습 세션을 진행합니다.
     * sessionId가 제공되면 기존 세션을 이어가고, 없으면 새로 시작합니다.
     * 최대 5번 상호작용 후 이해도 점수가 포함된 결과가 반환됩니다.
     *
     * @param challengeId 대화 대상 챌린지 ID
     * @param request 사용자 ID, 메시지, 세션 ID(선택)
     * @return 업데이트된 학습 세션 정보
     */
    @PostMapping("/{challengeId}/chats")
    fun chatAboutChallenge(
        @PathVariable challengeId: String,
        @RequestBody request: ChallengeChatRequest
    ): ResponseEntity<ChallengeChatResponse> {
        try {
            val response = chatService.processChat(challengeId, request)
            return ResponseEntity.ok(response)
        } catch (e: IllegalArgumentException) {
            // Log the error
            return ResponseEntity.badRequest().build()
        } catch (e: Exception) {
            // Log the error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    /**
     * 특정 챌린지에 대한 사용자의 모든 채팅 세션 목록을 조회합니다.
     *
     * @param challengeId 조회할 챌린지 ID
     * @param userId 조회할 사용자 ID (쿼리 파라미터로 전달)
     * @return 해당 사용자의 해당 챌린지에 대한 모든 채팅 세션 목록
     */
    @GetMapping("/{challengeId}/chats")
    fun getChatSessionsByChallenge(
        @PathVariable challengeId: String,
        @RequestParam userId: String
    ): ResponseEntity<List<ChallengeChatResponse>> {
        try {
            val chatSessions = chatService.getChatSessionsByChallengeAndUser(challengeId, userId)
            return ResponseEntity.ok(chatSessions)
        } catch (e: IllegalArgumentException) {
            // 챌린지가 존재하지 않는 경우 등의 예외 처리
            return ResponseEntity.badRequest().build()
        } catch (e: Exception) {
            // 기타 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }
}
