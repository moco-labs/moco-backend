package lab.ujumeonji.moco.support.error

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val status: HttpStatus,
    val code: String,
    val message: String,
) {
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH001", "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "AUTH002", "접근 권한이 없습니다."),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SYS001", "시스템 오류가 발생했습니다."),

    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "BIZ001", "요청한 리소스를 찾을 수 없습니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "BIZ002", "잘못된 요청입니다."),
}
