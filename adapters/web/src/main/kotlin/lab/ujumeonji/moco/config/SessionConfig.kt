package lab.ujumeonji.moco.config

import lab.ujumeonji.moco.support.session.TokenManager
import lab.ujumeonji.moco.support.session.impl.JwtTokenManager
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration(proxyBeanMethods = false)
class SessionConfig {
    @Bean
    fun tokenManager(
        @Value("\${jwt.secret}") secretKey: String,
        @Value("\${jwt.expiration}") tokenExpired: Long,
    ): TokenManager = JwtTokenManager(secretKey, tokenExpired)

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}
