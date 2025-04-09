package lab.ujumeonji.chatalgoapi.model

import org.springframework.ai.chat.messages.MessageType
import java.time.Instant

data class ChatMessage(
    val type: MessageType, // USER or ASSISTANT
    val content: String,
    val timestamp: Instant = Instant.now()
) 