package lab.ujumeonji.moco.support.error

enum class ErrorCode(
    val httpStatus: Int,
    val code: String,
    val message: String,
) {
    UNAUTHORIZED(401, "AUTH001", "인증이 필요합니다."),
    FORBIDDEN(403, "AUTH002", "접근 권한이 없습니다."),
    AUTHENTICATION_FAILED(401, "AUTH003", "인증에 실패했습니다."),
    TOKEN_EXPIRED(401, "AUTH004", "토큰이 만료되었습니다."),
    INVALID_TOKEN(401, "AUTH005", "유효하지 않은 토큰입니다."),

    EMAIL_ALREADY_EXISTS(400, "USER001", "이미 존재하는 이메일입니다."),
    PASSWORD_MISMATCH(400, "USER002", "비밀번호가 일치하지 않습니다."),
    USER_NOT_FOUND(404, "USER003", "사용자를 찾을 수 없습니다."),
    INVALID_USER_INFO(400, "USER004", "유효하지 않은 사용자 정보입니다."),

    CHALLENGE_NOT_FOUND(404, "CHALLENGE001", "챌린지를 찾을 수 없습니다."),
    INVALID_CHALLENGE_DATA(400, "CHALLENGE002", "유효하지 않은 챌린지 데이터입니다."),
    CHALLENGE_CREATION_FAILED(500, "CHALLENGE003", "챌린지 생성에 실패했습니다."),

    LESSON_NOT_FOUND(404, "LESSON001", "레슨을 찾을 수 없습니다."),
    INVALID_LESSON_DATA(400, "LESSON002", "유효하지 않은 레슨 데이터입니다."),
    LESSON_CREATION_FAILED(500, "LESSON003", "레슨 생성에 실패했습니다."),

    CHAT_PROCESSING_FAILED(500, "CHAT001", "채팅 처리에 실패했습니다."),
    INVALID_CHAT_REQUEST(400, "CHAT002", "유효하지 않은 채팅 요청입니다."),

    INTERNAL_SERVER_ERROR(500, "SYS001", "시스템 오류가 발생했습니다."),
    DATABASE_ERROR(500, "SYS002", "데이터베이스 오류가 발생했습니다."),
    EXTERNAL_API_ERROR(500, "SYS003", "외부 API 호출에 실패했습니다."),

    RESOURCE_NOT_FOUND(404, "BIZ001", "요청한 리소스를 찾을 수 없습니다."),
    INVALID_REQUEST(400, "BIZ002", "잘못된 요청입니다."),
    VALIDATION_FAILED(400, "BIZ003", "입력값 검증에 실패했습니다."),
}
