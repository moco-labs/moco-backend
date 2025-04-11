package lab.ujumeonji.chatalgoapi.support.exception

import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime

@RestControllerAdvice
class GlobalExceptionHandler {

    private val logger = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(ex: BusinessException): ResponseEntity<ErrorResponse> {
        logger.error("Business exception occurred: {}", ex.message)
        
        val response = ErrorResponse(
            timestamp = LocalDateTime.now(),
            status = ex.errorCode.status.value(),
            code = ex.errorCode.code,
            message = ex.message
        )
        
        return ResponseEntity.status(ex.errorCode.status).body(response)
    }

    @ExceptionHandler(Exception::class)
    fun handleUnexpectedException(ex: Exception): ResponseEntity<ErrorResponse> {
        logger.error("Unexpected exception occurred", ex)
        
        val errorCode = ErrorCode.INTERNAL_SERVER_ERROR
        val response = ErrorResponse(
            timestamp = LocalDateTime.now(),
            status = errorCode.status.value(),
            code = errorCode.code,
            message = errorCode.message
        )
        
        return ResponseEntity.status(errorCode.status).body(response)
    }
}
