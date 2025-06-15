package lab.ujumeonji.moco.support.auth

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lab.ujumeonji.moco.support.session.TokenManager
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.cors.CorsUtils
import org.springframework.web.servlet.HandlerInterceptor

@Component
class AuthInterceptor(
    private val tokenManager: TokenManager,
    private val authContext: AuthContext,
    private val authNotRequiredConditions: MutableSet<UriAndMethodsCondition> = mutableSetOf(),
) : HandlerInterceptor {
    fun setAuthNotRequiredConditions(vararg conditions: UriAndMethodsCondition) {
        authNotRequiredConditions.clear()
        authNotRequiredConditions.addAll(conditions)
    }

    fun addAuthNotRequiredCondition(uriPattern: String, vararg httpMethods: HttpMethod) {
        authNotRequiredConditions.add(UriAndMethodsCondition(uriPattern, httpMethods.toSet()))
    }

    fun addAuthNotRequiredConditions(conditions: Collection<UriAndMethodsCondition>) {
        authNotRequiredConditions.addAll(conditions)
    }

    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
    ): Boolean {
        if (CorsUtils.isPreFlightRequest(request)) {
            return true
        }

        if (isAuthenticationNotRequired(request)) {
            return true
        }

        val userId = extractUserFromJwt(request)
        authContext.accountId = userId

        return true
    }

    private fun isAuthenticationNotRequired(request: HttpServletRequest): Boolean {
        val httpMethod = HttpMethod.valueOf(request.method)
        val requestURI = request.requestURI
        return authNotRequiredConditions.any { it.match(requestURI, httpMethod) }
    }

    private fun extractUserFromJwt(request: HttpServletRequest): String? {
        val authHeader = request.getHeader("Authorization") ?: return null
        if (!authHeader.startsWith("Bearer ")) return null

        return runCatching {
            val token = authHeader.removePrefix("Bearer ")
            val claims = tokenManager.verifyToken(token)
            claims["id"]?.toString()
        }.getOrNull()
    }

    data class UriAndMethodsCondition(
        val uriPattern: String,
        val httpMethods: Set<HttpMethod>,
    ) {
        fun match(
            requestURI: String,
            httpMethod: HttpMethod,
        ): Boolean {
            return requestURI == uriPattern && httpMethods.contains(httpMethod)
        }
    }
}
