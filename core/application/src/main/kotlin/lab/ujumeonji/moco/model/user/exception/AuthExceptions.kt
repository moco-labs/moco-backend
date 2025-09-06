package lab.ujumeonji.moco.model.user.exception

import lab.ujumeonji.moco.support.error.BusinessException
import lab.ujumeonji.moco.support.error.ErrorCode

class EmailAlreadyExistsException(message: String? = null) : BusinessException(
    ErrorCode.EMAIL_ALREADY_EXISTS,
    message ?: ErrorCode.EMAIL_ALREADY_EXISTS.message,
)

class PasswordMismatchException(message: String? = null) : BusinessException(
    ErrorCode.PASSWORD_MISMATCH,
    message ?: ErrorCode.PASSWORD_MISMATCH.message,
)

class AuthenticationFailedException(message: String? = null) : BusinessException(
    ErrorCode.AUTHENTICATION_FAILED,
    message ?: ErrorCode.AUTHENTICATION_FAILED.message,
)
