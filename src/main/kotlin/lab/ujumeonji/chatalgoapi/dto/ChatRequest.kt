package lab.ujumeonji.chatalgoapi.dto

data class ChatRequest(
    val userId: String,
    val message: String,
    val conversationId: String? = null,
)
