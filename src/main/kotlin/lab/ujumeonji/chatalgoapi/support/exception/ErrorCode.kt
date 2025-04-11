package lab.ujumeonji.chatalgoapi.support.exception

import org.springframework.http.HttpStatus

/**
 * 시스템에서 사용하는 오류 코드 정의
 */
enum class ErrorCode(
    val status: HttpStatus,
    val code: String,
    val message: String
) {
    // 인증 관련 오류
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH001", "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "AUTH002", "접근 권한이 없습니다."),
    
    // 기타 시스템 오류
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SYS001", "시스템 오류가 발생했습니다."),
    
    // 비즈니스 로직 오류
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "BIZ001", "요청한 리소스를 찾을 수 없습니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "BIZ002", "잘못된 요청입니다."),
    
    // 기타 오류
    ;
}
