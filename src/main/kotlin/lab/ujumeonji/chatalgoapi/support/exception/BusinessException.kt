package lab.ujumeonji.chatalgoapi.support.exception

/**
 * 비즈니스 로직에서 발생하는 예외를 표현하는 클래스
 */
class BusinessException(
    val errorCode: ErrorCode,
    override val message: String = errorCode.message,
    override val cause: Throwable? = null
) : RuntimeException(message, cause)
