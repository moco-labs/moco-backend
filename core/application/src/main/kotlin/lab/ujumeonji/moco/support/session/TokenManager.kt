package lab.ujumeonji.moco.support.session

import java.time.LocalDateTime

interface TokenManager {
    fun createToken(
        payload: Map<String, *>,
        issuedAt: LocalDateTime,
    ): String

    fun verifyToken(token: String): Map<String, *>
}
