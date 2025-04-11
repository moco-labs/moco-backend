package lab.ujumeonji.chatalgoapi.support.auth

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lab.ujumeonji.chatalgoapi.support.exception.BusinessException
import lab.ujumeonji.chatalgoapi.support.exception.ErrorCode
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor

/**
 * 요청 핸들러에 대한 권한 검사를 수행하는 인터셉터
 * RequiredRole 어노테이션이 있는 컨트롤러 메서드나 클래스에 대해 역할 기반 접근 제어를 수행합니다.
 */
@Component
class AuthorizationInterceptor(
    private val authContext: AuthContext
) : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (handler !is HandlerMethod) {
            return true
        }

        // 메서드에 RequiredRole 어노테이션이 있는지 확인
        val methodAnnotation = AnnotationUtils.findAnnotation(handler.method, RequiredRole::class.java)
        if (methodAnnotation != null) {
            checkAuthorization(methodAnnotation.roles)
            return true
        }

        // 클래스에 RequiredRole 어노테이션이 있는지 확인
        val classAnnotation = AnnotationUtils.findAnnotation(handler.beanType, RequiredRole::class.java)
        if (classAnnotation != null) {
            checkAuthorization(classAnnotation.roles)
            return true
        }

        // 어노테이션이 없으면 접근 허용
        return true
    }

    /**
     * 사용자가 필요한 역할을 가지고 있는지 확인합니다.
     * 
     * @param requiredRoles 접근에 필요한 역할 목록
     * @throws BusinessException 권한이 없을 경우 FORBIDDEN 예외 발생
     */
    private fun checkAuthorization(requiredRoles: Array<Role>) {
        if (authContext.accountId == null) {
            throw BusinessException(ErrorCode.UNAUTHORIZED)
        }

        if (!authContext.hasAnyRole(requiredRoles)) {
            throw BusinessException(ErrorCode.FORBIDDEN)
        }
    }
}
