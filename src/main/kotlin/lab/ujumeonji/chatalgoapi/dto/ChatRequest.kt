package lab.ujumeonji.chatalgoapi.dto

data class ChatRequest(
    val userId: String,
    val message: String,
    val conversationId: String? = null // 기존 대화에 이어서 할 경우 ID 전달
) 