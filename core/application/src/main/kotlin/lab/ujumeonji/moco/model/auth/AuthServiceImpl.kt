package lab.ujumeonji.moco.model.auth

import lab.ujumeonji.moco.model.user.UserService
import lab.ujumeonji.moco.model.user.io.AuthOutput
import lab.ujumeonji.moco.model.user.io.SignInInput
import lab.ujumeonji.moco.model.user.io.SignUpInput
import lab.ujumeonji.moco.model.user.io.TokenOutput
import lab.ujumeonji.moco.model.user.io.UserProfileOutput
import lab.ujumeonji.moco.support.session.TokenManager
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class AuthServiceImpl(
    private val userService: UserService,
    private val tokenManager: TokenManager,
) : AuthService {
    override fun signUp(input: SignUpInput): AuthOutput {
        val user = userService.signUp(input)
        return AuthOutput(
            success = true,
            message = "Registration successful",
            userId = user.id,
            email = user.email,
            name = user.name,
        )
    }

    override fun signIn(input: SignInInput): TokenOutput {
        val user = userService.login(input)

        val token =
            tokenManager.createToken(
                mapOf("id" to user.id),
                issuedAt = LocalDateTime.now(),
            )

        return TokenOutput(
            success = true,
            message = "Login successful",
            accessToken = token,
            userId = user.id,
            name = user.name,
            email = user.email,
        )
    }

    override fun getMyProfile(userId: String): UserProfileOutput {
        val user =
            userService.findById(userId)
                ?: throw IllegalArgumentException("User not found: $userId")

        return UserProfileOutput(
            id = user.id ?: "",
            name = user.name,
            email = user.email,
        )
    }
}
