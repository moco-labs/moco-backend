package lab.ujumeonji.chatalgoapi.support.session.impl

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*
import lab.ujumeonji.chatalgoapi.support.session.TokenManager

class JwtTokenManager(
    private val secret: String,
    private val accessTokenValidity: Long,
) : TokenManager {

    override fun createToken(payload: Map<String, *>, issuedAt: LocalDateTime): String =
        with(issuedAt.toInstant(ZoneOffset.UTC)) {
            val now = Date.from(this)
            val expirationDate = Date(now.time + accessTokenValidity)

            Jwts.builder()
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(Keys.hmacShaKeyFor(secret.toByteArray()))
                .claims(payload)
                .compact()
        }

    override fun verifyToken(token: String): Map<String, *> =
        Jwts.parser()
            .verifyWith(Keys.hmacShaKeyFor(secret.toByteArray()))
            .build()
            .parseSignedClaims(token)
            .payload
}
