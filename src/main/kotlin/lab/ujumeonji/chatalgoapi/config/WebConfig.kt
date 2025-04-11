package lab.ujumeonji.chatalgoapi.config

import lab.ujumeonji.chatalgoapi.support.auth.AuthInterceptor
import lab.ujumeonji.chatalgoapi.support.auth.AuthorizationInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:3000", "https://quibe.otter.coffee")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true)
            .maxAge(3600)
    }
}

@Configuration
class WebConfig(
    private val authInterceptor: AuthInterceptor,
    private val authorizationInterceptor: AuthorizationInterceptor,
    private val authArgumentResolver: AuthArgumentResolver
) : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        // 인증 인터셉터 등록
        registry.addInterceptor(authInterceptor)
            .addPathPatterns("/**") // 모든 경로에 적용
            .order(1) // 인증 먼저 처리

        // 인가 인터셉터 등록 (인증 이후에 실행되어야 함)
        registry.addInterceptor(authorizationInterceptor)
            .addPathPatterns("/**") // 모든 경로에 적용
            .order(2) // 인증 다음에 인가 처리
    }

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(authArgumentResolver)
    }
}