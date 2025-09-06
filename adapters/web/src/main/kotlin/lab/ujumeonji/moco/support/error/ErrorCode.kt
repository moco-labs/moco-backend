package lab.ujumeonji.moco.support.error

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val status: HttpStatus,
    val code: String,
    val message: String,
) {
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH001", "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "AUTH002", "접근 권한이 없습니다."),
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "AUTH003", "인증에 실패했습니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "AUTH004", "토큰이 만료되었습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH005", "유효하지 않은 토큰입니다."),

    EMAIL_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "USER001", "이미 존재하는 이메일입니다."),
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "USER002", "비밀번호가 일치하지 않습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER003", "사용자를 찾을 수 없습니다."),
    INVALID_USER_INFO(HttpStatus.BAD_REQUEST, "USER004", "유효하지 않은 사용자 정보입니다."),

    CHALLENGE_NOT_FOUND(HttpStatus.NOT_FOUND, "CHALLENGE001", "챌린지를 찾을 수 없습니다."),
    INVALID_CHALLENGE_DATA(HttpStatus.BAD_REQUEST, "CHALLENGE002", "유효하지 않은 챌린지 데이터입니다."),
    CHALLENGE_CREATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "CHALLENGE003", "챌린지 생성에 실패했습니다."),

    LESSON_NOT_FOUND(HttpStatus.NOT_FOUND, "LESSON001", "레슨을 찾을 수 없습니다."),
    INVALID_LESSON_DATA(HttpStatus.BAD_REQUEST, "LESSON002", "유효하지 않은 레슨 데이터입니다."),
    LESSON_CREATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "LESSON003", "레슨 생성에 실패했습니다."),

    CHAT_PROCESSING_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "CHAT001", "채팅 처리에 실패했습니다."),
    INVALID_CHAT_REQUEST(HttpStatus.BAD_REQUEST, "CHAT002", "유효하지 않은 채팅 요청입니다."),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SYS001", "시스템 오류가 발생했습니다."),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SYS002", "데이터베이스 오류가 발생했습니다."),
    EXTERNAL_API_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SYS003", "외부 API 호출에 실패했습니다."),

    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "BIZ001", "요청한 리소스를 찾을 수 없습니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "BIZ002", "잘못된 요청입니다."),
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "BIZ003", "입력값 검증에 실패했습니다."),
}
