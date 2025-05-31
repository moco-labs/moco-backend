package lab.ujumeonji.moco.support.auth

import lab.ujumeonji.moco.support.error.BusinessException
import lab.ujumeonji.moco.support.error.ErrorCode
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class AuthArgumentResolver(
    private val authContext: AuthContext,
) : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean =
        parameter.hasParameterAnnotation(RequiredAuth::class.java) && parameter.parameterType == String::class.java

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): String? =
        when {
            parameter.hasParameterAnnotation(RequiredAuth::class.java) &&
                parameter.parameterType == String::class.java ->
                authContext.accountId ?: throw BusinessException(ErrorCode.UNAUTHORIZED)

            else -> authContext.accountId
        }
}
