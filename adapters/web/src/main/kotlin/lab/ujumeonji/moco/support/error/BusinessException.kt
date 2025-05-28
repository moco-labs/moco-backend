package lab.ujumeonji.moco.support.error

class BusinessException(
    val errorCode: ErrorCode,
    override val message: String = errorCode.message,
    override val cause: Throwable? = null,
) : RuntimeException(message, cause)
