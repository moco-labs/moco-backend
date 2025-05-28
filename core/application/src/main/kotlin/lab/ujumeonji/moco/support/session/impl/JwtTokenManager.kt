package lab.ujumeonji.moco.support.session.impl

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import lab.ujumeonji.moco.support.session.TokenManager
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Date

class JwtTokenManager(
    private val secretKey: String,
    private val tokenExpired: Long,
) : TokenManager {
    override fun createToken(
        payload: Map<String, *>,
        issuedAt: LocalDateTime,
    ): String =
        with(issuedAt.toInstant(ZoneOffset.UTC)) {
            val now = Date.from(this)
            val expirationDate = Date(now.time + tokenExpired)

            Jwts.builder()
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(Keys.hmacShaKeyFor(secretKey.toByteArray()))
                .claims(payload)
                .compact()
        }

    override fun verifyToken(token: String): Map<String, *> =
        Jwts.parser()
            .verifyWith(Keys.hmacShaKeyFor(secretKey.toByteArray()))
            .build()
            .parseSignedClaims(token)
            .payload
}
