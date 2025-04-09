package lab.ujumeonji.chatalgoapi.controller

import lab.ujumeonji.chatalgoapi.dto.ChatRequest
import lab.ujumeonji.chatalgoapi.model.Conversation
import lab.ujumeonji.chatalgoapi.service.ChatService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/chat") // API 경로 변경 (선택사항)
class ChatController(private val chatService: ChatService) {

    @PostMapping("/message")
    fun chat(@RequestBody request: ChatRequest): ResponseEntity<Conversation> {
        val updatedConversation = chatService.chat(request.userId, request.message, request.conversationId)
        return ResponseEntity.ok(updatedConversation)
    }

    @GetMapping("/history/{conversationId}")
    fun getConversationHistory(@PathVariable conversationId: String): ResponseEntity<Conversation> {
        val conversation = chatService.getConversationHistory(conversationId)
        return if (conversation != null) {
            ResponseEntity.ok(conversation)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/user/{userId}")
    fun getUserConversations(@PathVariable userId: String): ResponseEntity<List<Conversation>> {
        val conversations = chatService.getUserConversations(userId)
        return ResponseEntity.ok(conversations)
    }
} 