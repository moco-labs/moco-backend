package lab.ujumeonji.chatalgoapi.support.exception

import java.time.LocalDateTime

data class ErrorResponse(
    val timestamp: LocalDateTime,
    val status: Int,
    val code: String,
    val message: String,
)
