package lab.ujumeonji.chatalgoapi.config

import lab.ujumeonji.chatalgoapi.support.session.TokenManager
import lab.ujumeonji.chatalgoapi.support.session.impl.JwtTokenManager
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class AppConfig {

    @Bean
    fun tokenManager(
        @Value("\${jwt.secret-key}") secretKey: String,
        @Value("\${jwt.token-expired}") tokenExpired: Long,
    ): TokenManager = JwtTokenManager(secretKey, tokenExpired)

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}
