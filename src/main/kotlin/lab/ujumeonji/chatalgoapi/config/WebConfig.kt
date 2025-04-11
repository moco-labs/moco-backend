package lab.ujumeonji.chatalgoapi.config

import lab.ujumeonji.chatalgoapi.support.auth.AuthArgumentResolver
import lab.ujumeonji.chatalgoapi.support.auth.AuthInterceptor
import lab.ujumeonji.chatalgoapi.support.session.TokenManager
import lab.ujumeonji.chatalgoapi.support.session.impl.JwtTokenManager
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(
    private val authInterceptor: AuthInterceptor,
    private val authArgumentResolver: AuthArgumentResolver
) : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(authInterceptor)
    }

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(authArgumentResolver)
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:3000", "https://quibe.otter.coffee")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true)
            .maxAge(3600)
    }

    @Bean
    fun tokenManager(
        @Value("\${jwt.secret-key}") secretKey: String,
        @Value("\${jwt.token-expired}") tokenExpired: Long,
    ): TokenManager = JwtTokenManager(secretKey, tokenExpired)
}
