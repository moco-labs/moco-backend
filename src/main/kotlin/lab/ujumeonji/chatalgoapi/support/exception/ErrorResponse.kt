package lab.ujumeonji.chatalgoapi.support.exception

import java.time.LocalDateTime

/**
 * API 오류 응답 형식
 *
 * @property timestamp 오류 발생 시간
 * @property status HTTP 상태 코드
 * @property code 시스템 내부 오류 코드
 * @property message 오류 메시지
 */
data class ErrorResponse(
    val timestamp: LocalDateTime,
    val status: Int,
    val code: String,
    val message: String
)
