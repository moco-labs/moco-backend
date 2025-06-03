package lab.ujumeonji.moco.controller.auth

import jakarta.validation.Valid
import lab.ujumeonji.moco.controller.auth.dto.AuthResponse
import lab.ujumeonji.moco.controller.auth.dto.SignInRequest
import lab.ujumeonji.moco.controller.auth.dto.SignUpRequest
import lab.ujumeonji.moco.controller.auth.dto.TokenResponse
import lab.ujumeonji.moco.controller.auth.dto.UserProfileResponse
import lab.ujumeonji.moco.model.user.UserService
import lab.ujumeonji.moco.model.user.exception.AuthenticationFailedException
import lab.ujumeonji.moco.model.user.exception.EmailAlreadyExistsException
import lab.ujumeonji.moco.model.user.exception.PasswordMismatchException
import lab.ujumeonji.moco.model.user.io.AuthOutput
import lab.ujumeonji.moco.model.user.io.TokenOutput
import lab.ujumeonji.moco.model.user.io.UserProfileOutput
import lab.ujumeonji.moco.support.auth.RequiredAuth
import lab.ujumeonji.moco.support.session.TokenManager
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val userService: UserService,
    private val jwtService: TokenManager,
) {
    private val logger = LoggerFactory.getLogger(AuthController::class.java)

    @PostMapping("/signup")
    fun signup(
        @Valid @RequestBody request: SignUpRequest,
        bindingResult: BindingResult,
    ): ResponseEntity<AuthResponse> {
        if (bindingResult.hasErrors()) {
            val errorMessage = bindingResult.fieldErrors.joinToString(", ") { "${it.field}: ${it.defaultMessage}" }
            val output = AuthOutput(false, errorMessage)
            return ResponseEntity
                .badRequest()
                .body(AuthResponse.from(output))
        }

        try {
            val user = userService.signUp(request.toInput())

            val output = AuthOutput(
                success = true,
                message = "Registration successful",
                userId = user.id,
                email = user.email,
                name = user.name,
            )

            return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(AuthResponse.from(output))
        } catch (e: EmailAlreadyExistsException) {
            val output = AuthOutput(false, e.message ?: "Email already exists")
            return ResponseEntity
                .badRequest()
                .body(AuthResponse.from(output))
        } catch (e: PasswordMismatchException) {
            val output = AuthOutput(false, e.message ?: "Password and confirmation don't match")
            return ResponseEntity
                .badRequest()
                .body(AuthResponse.from(output))
        } catch (e: Exception) {
            logger.error("Error during user registration", e)
            val output = AuthOutput(false, "An unexpected error occurred")
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(AuthResponse.from(output))
        }
    }

    @PostMapping("/signin")
    fun login(
        @Valid @RequestBody request: SignInRequest,
        bindingResult: BindingResult,
    ): ResponseEntity<TokenResponse> {
        if (bindingResult.hasErrors()) {
            val errorMessage = bindingResult.fieldErrors.joinToString(", ") { "${it.field}: ${it.defaultMessage}" }
            val output = TokenOutput(false, errorMessage)
            return ResponseEntity
                .badRequest()
                .body(TokenResponse.from(output))
        }

        try {
            val user = userService.login(request.toInput())

            val token =
                jwtService.createToken(
                    mapOf(
                        "id" to user.id,
                    ),
                    issuedAt = LocalDateTime.now(),
                )

            val output = TokenOutput(
                success = true,
                message = "Login successful",
                accessToken = token,
                userId = user.id,
                name = user.name,
                email = user.email,
            )

            return ResponseEntity.ok(TokenResponse.from(output))
        } catch (e: AuthenticationFailedException) {
            val output = TokenOutput(false, e.message ?: "Authentication failed")
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(TokenResponse.from(output))
        } catch (e: Exception) {
            logger.error("Error during user login", e)
            val output = TokenOutput(false, "An unexpected error occurred")
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(TokenResponse.from(output))
        }
    }

    @GetMapping("/me")
    fun getMyProfile(
        @RequiredAuth userId: String,
    ): ResponseEntity<UserProfileResponse> {
        try {
            val user =
                userService.findById(userId)
                    ?: return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(
                            UserProfileResponse.from(
                                UserProfileOutput(
                                    id = "",
                                    name = "",
                                    email = "",
                                )
                            )
                        )

            val output = UserProfileOutput(
                id = user.id ?: "",
                name = user.name,
                email = user.email,
            )

            return ResponseEntity.ok(UserProfileResponse.from(output))
        } catch (e: Exception) {
            logger.error("Error retrieving user profile", e)
            val output = UserProfileOutput(
                id = "",
                name = "",
                email = "",
            )
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(UserProfileResponse.from(output))
        }
    }

    @PostMapping("/signup/google")
    fun signupWithGoogle(): ResponseEntity<AuthResponse> {
        val output = AuthOutput(false, "Google signup not yet implemented")
        return ResponseEntity
            .status(HttpStatus.NOT_IMPLEMENTED)
            .body(AuthResponse.from(output))
    }

    @PostMapping("/signup/facebook")
    fun signupWithFacebook(): ResponseEntity<AuthResponse> {
        val output = AuthOutput(false, "Facebook signup not yet implemented")
        return ResponseEntity
            .status(HttpStatus.NOT_IMPLEMENTED)
            .body(AuthResponse.from(output))
    }
}
