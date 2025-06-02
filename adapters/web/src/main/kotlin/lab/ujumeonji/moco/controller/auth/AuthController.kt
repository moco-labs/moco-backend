package lab.ujumeonji.moco.controller.auth

import jakarta.validation.Valid
import lab.ujumeonji.moco.model.user.UserService
import lab.ujumeonji.moco.model.user.exception.AuthenticationFailedException
import lab.ujumeonji.moco.model.user.exception.EmailAlreadyExistsException
import lab.ujumeonji.moco.model.user.exception.PasswordMismatchException
import lab.ujumeonji.moco.model.user.io.AuthOutput
import lab.ujumeonji.moco.model.user.io.SignInInput
import lab.ujumeonji.moco.model.user.io.SignUpInput
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
        @Valid @RequestBody request: SignUpInput,
        bindingResult: BindingResult,
    ): ResponseEntity<AuthOutput> {
        if (bindingResult.hasErrors()) {
            val errorMessage = bindingResult.fieldErrors.joinToString(", ") { "${it.field}: ${it.defaultMessage}" }
            return ResponseEntity
                .badRequest()
                .body(AuthOutput(false, errorMessage))
        }

        try {
            val user = userService.signUp(request)

            return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                    AuthOutput(
                        success = true,
                        message = "Registration successful",
                        userId = user.id,
                        email = user.email,
                        name = user.name,
                    ),
                )
        } catch (e: EmailAlreadyExistsException) {
            return ResponseEntity
                .badRequest()
                .body(AuthOutput(false, e.message ?: "Email already exists"))
        } catch (e: PasswordMismatchException) {
            return ResponseEntity
                .badRequest()
                .body(AuthOutput(false, e.message ?: "Password and confirmation don't match"))
        } catch (e: Exception) {
            logger.error("Error during user registration", e)
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(AuthOutput(false, "An unexpected error occurred"))
        }
    }

    @PostMapping("/signin")
    fun login(
        @Valid @RequestBody request: SignInInput,
        bindingResult: BindingResult,
    ): ResponseEntity<TokenOutput> {
        if (bindingResult.hasErrors()) {
            val errorMessage = bindingResult.fieldErrors.joinToString(", ") { "${it.field}: ${it.defaultMessage}" }
            return ResponseEntity
                .badRequest()
                .body(TokenOutput(false, errorMessage))
        }

        try {
            val user = userService.login(request)

            val token =
                jwtService.createToken(
                    mapOf(
                        "id" to user.id,
                    ),
                    issuedAt = LocalDateTime.now(),
                )

            return ResponseEntity.ok(
                TokenOutput(
                    success = true,
                    message = "Login successful",
                    accessToken = token,
                    userId = user.id,
                    name = user.name,
                    email = user.email,
                ),
            )
        } catch (e: AuthenticationFailedException) {
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(TokenOutput(false, e.message ?: "Authentication failed"))
        } catch (e: Exception) {
            logger.error("Error during user login", e)
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(TokenOutput(false, "An unexpected error occurred"))
        }
    }

    @GetMapping("/me")
    fun getMyProfile(
        @RequiredAuth userId: String,
    ): ResponseEntity<UserProfileOutput> {
        try {
            val user =
                userService.findById(userId)
                    ?: return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(
                            UserProfileOutput(
                                id = "",
                                name = "",
                                email = "",
                            ),
                        )

            return ResponseEntity.ok(
                UserProfileOutput(
                    id = user.id ?: "",
                    name = user.name,
                    email = user.email,
                ),
            )
        } catch (e: Exception) {
            logger.error("Error retrieving user profile", e)
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                    UserProfileOutput(
                        id = "",
                        name = "",
                        email = "",
                    ),
                )
        }
    }

    @PostMapping("/signup/google")
    fun signupWithGoogle(): ResponseEntity<AuthOutput> {
        return ResponseEntity
            .status(HttpStatus.NOT_IMPLEMENTED)
            .body(AuthOutput(false, "Google signup not yet implemented"))
    }

    @PostMapping("/signup/facebook")
    fun signupWithFacebook(): ResponseEntity<AuthOutput> {
        return ResponseEntity
            .status(HttpStatus.NOT_IMPLEMENTED)
            .body(AuthOutput(false, "Facebook signup not yet implemented"))
    }
}
