package lab.ujumeonji.moco.support.error

class BusinessException(
    val errorCode: ErrorCode,
    override val message: String = errorCode.message,
    override val cause: Throwable? = null,
) : RuntimeException(message, cause) {
    companion object {
        fun emailAlreadyExists(email: String? = null): BusinessException {
            val message = email?.let { "이미 존재하는 이메일입니다: $it" } ?: ErrorCode.EMAIL_ALREADY_EXISTS.message
            return BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS, message)
        }

        fun userNotFound(userId: String? = null): BusinessException {
            val message = userId?.let { "사용자를 찾을 수 없습니다: $it" } ?: ErrorCode.USER_NOT_FOUND.message
            return BusinessException(ErrorCode.USER_NOT_FOUND, message)
        }

        fun passwordMismatch(): BusinessException {
            return BusinessException(ErrorCode.PASSWORD_MISMATCH)
        }

        fun authenticationFailed(reason: String? = null): BusinessException {
            val message = reason ?: ErrorCode.AUTHENTICATION_FAILED.message
            return BusinessException(ErrorCode.AUTHENTICATION_FAILED, message)
        }

        fun invalidToken(): BusinessException {
            return BusinessException(ErrorCode.INVALID_TOKEN)
        }

        fun tokenExpired(): BusinessException {
            return BusinessException(ErrorCode.TOKEN_EXPIRED)
        }

        fun challengeNotFound(challengeId: String? = null): BusinessException {
            val message = challengeId?.let { "챌린지를 찾을 수 없습니다: $it" } ?: ErrorCode.CHALLENGE_NOT_FOUND.message
            return BusinessException(ErrorCode.CHALLENGE_NOT_FOUND, message)
        }

        fun invalidChallengeData(reason: String? = null): BusinessException {
            val message = reason ?: ErrorCode.INVALID_CHALLENGE_DATA.message
            return BusinessException(ErrorCode.INVALID_CHALLENGE_DATA, message)
        }

        fun challengeCreationFailed(reason: String? = null): BusinessException {
            val message = reason ?: ErrorCode.CHALLENGE_CREATION_FAILED.message
            return BusinessException(ErrorCode.CHALLENGE_CREATION_FAILED, message)
        }

        fun lessonNotFound(lessonId: String? = null): BusinessException {
            val message = lessonId?.let { "레슨을 찾을 수 없습니다: $it" } ?: ErrorCode.LESSON_NOT_FOUND.message
            return BusinessException(ErrorCode.LESSON_NOT_FOUND, message)
        }

        fun invalidLessonData(reason: String? = null): BusinessException {
            val message = reason ?: ErrorCode.INVALID_LESSON_DATA.message
            return BusinessException(ErrorCode.INVALID_LESSON_DATA, message)
        }

        fun lessonCreationFailed(reason: String? = null): BusinessException {
            val message = reason ?: ErrorCode.LESSON_CREATION_FAILED.message
            return BusinessException(ErrorCode.LESSON_CREATION_FAILED, message)
        }

        fun chatProcessingFailed(reason: String? = null): BusinessException {
            val message = reason ?: ErrorCode.CHAT_PROCESSING_FAILED.message
            return BusinessException(ErrorCode.CHAT_PROCESSING_FAILED, message)
        }

        fun invalidChatRequest(reason: String? = null): BusinessException {
            val message = reason ?: ErrorCode.INVALID_CHAT_REQUEST.message
            return BusinessException(ErrorCode.INVALID_CHAT_REQUEST, message)
        }

        fun resourceNotFound(resource: String? = null): BusinessException {
            val message = resource?.let { "리소스를 찾을 수 없습니다: $it" } ?: ErrorCode.RESOURCE_NOT_FOUND.message
            return BusinessException(ErrorCode.RESOURCE_NOT_FOUND, message)
        }

        fun invalidRequest(reason: String? = null): BusinessException {
            val message = reason ?: ErrorCode.INVALID_REQUEST.message
            return BusinessException(ErrorCode.INVALID_REQUEST, message)
        }

        fun validationFailed(reason: String? = null): BusinessException {
            val message = reason ?: ErrorCode.VALIDATION_FAILED.message
            return BusinessException(ErrorCode.VALIDATION_FAILED, message)
        }

        fun unauthorized(reason: String? = null): BusinessException {
            val message = reason ?: ErrorCode.UNAUTHORIZED.message
            return BusinessException(ErrorCode.UNAUTHORIZED, message)
        }

        fun forbidden(reason: String? = null): BusinessException {
            val message = reason ?: ErrorCode.FORBIDDEN.message
            return BusinessException(ErrorCode.FORBIDDEN, message)
        }

        fun databaseError(reason: String? = null): BusinessException {
            val message = reason ?: ErrorCode.DATABASE_ERROR.message
            return BusinessException(ErrorCode.DATABASE_ERROR, message)
        }

        fun externalApiError(reason: String? = null): BusinessException {
            val message = reason ?: ErrorCode.EXTERNAL_API_ERROR.message
            return BusinessException(ErrorCode.EXTERNAL_API_ERROR, message)
        }
    }
}
