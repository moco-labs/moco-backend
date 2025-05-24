package lab.ujumeonji.chatalgoapi.controller

import jakarta.validation.Valid
import lab.ujumeonji.chatalgoapi.dto.AuthResponse
import lab.ujumeonji.chatalgoapi.dto.LoginRequest
import lab.ujumeonji.chatalgoapi.dto.SignupRequest
import lab.ujumeonji.chatalgoapi.dto.TokenResponse
import lab.ujumeonji.chatalgoapi.dto.UserProfileResponse
import lab.ujumeonji.chatalgoapi.exception.AuthenticationFailedException
import lab.ujumeonji.chatalgoapi.exception.EmailAlreadyExistsException
import lab.ujumeonji.chatalgoapi.exception.PasswordMismatchException
import lab.ujumeonji.chatalgoapi.service.UserService
import lab.ujumeonji.chatalgoapi.support.auth.RequiredAuth
import lab.ujumeonji.chatalgoapi.support.session.TokenManager
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
        @Valid @RequestBody request: SignupRequest,
        bindingResult: BindingResult,
    ): ResponseEntity<AuthResponse> {
        if (bindingResult.hasErrors()) {
            val errorMessage = bindingResult.fieldErrors.joinToString(", ") { "${it.field}: ${it.defaultMessage}" }
            return ResponseEntity
                .badRequest()
                .body(AuthResponse(false, errorMessage))
        }

        try {
            val user = userService.signUp(request)

            return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                    AuthResponse(
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
                .body(AuthResponse(false, e.message ?: "Email already exists"))
        } catch (e: PasswordMismatchException) {
            return ResponseEntity
                .badRequest()
                .body(AuthResponse(false, e.message ?: "Password and confirmation don't match"))
        } catch (e: Exception) {
            logger.error("Error during user registration", e)
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(AuthResponse(false, "An unexpected error occurred"))
        }
    }

    @PostMapping("/signin")
    fun login(
        @Valid @RequestBody request: LoginRequest,
        bindingResult: BindingResult,
    ): ResponseEntity<TokenResponse> {
        if (bindingResult.hasErrors()) {
            val errorMessage = bindingResult.fieldErrors.joinToString(", ") { "${it.field}: ${it.defaultMessage}" }
            return ResponseEntity
                .badRequest()
                .body(TokenResponse(false, errorMessage))
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
                TokenResponse(
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
                .body(TokenResponse(false, e.message ?: "Authentication failed"))
        } catch (e: Exception) {
            logger.error("Error during user login", e)
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(TokenResponse(false, "An unexpected error occurred"))
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
                            UserProfileResponse(
                                id = "",
                                name = "",
                                email = "",
                            ),
                        )

            return ResponseEntity.ok(
                UserProfileResponse(
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
                    UserProfileResponse(
                        id = "",
                        name = "",
                        email = "",
                    ),
                )
        }
    }

    @PostMapping("/signup/google")
    fun signupWithGoogle(): ResponseEntity<AuthResponse> {
        return ResponseEntity
            .status(HttpStatus.NOT_IMPLEMENTED)
            .body(AuthResponse(false, "Google signup not yet implemented"))
    }

    @PostMapping("/signup/facebook")
    fun signupWithFacebook(): ResponseEntity<AuthResponse> {
        return ResponseEntity
            .status(HttpStatus.NOT_IMPLEMENTED)
            .body(AuthResponse(false, "Facebook signup not yet implemented"))
    }
}
