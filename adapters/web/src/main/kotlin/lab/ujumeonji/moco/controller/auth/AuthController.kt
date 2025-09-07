package lab.ujumeonji.moco.controller.auth

import jakarta.validation.Valid
import lab.ujumeonji.moco.controller.auth.dto.AuthResponse
import lab.ujumeonji.moco.controller.auth.dto.SignInRequest
import lab.ujumeonji.moco.controller.auth.dto.SignUpRequest
import lab.ujumeonji.moco.controller.auth.dto.TokenResponse
import lab.ujumeonji.moco.controller.auth.dto.UserProfileResponse
import lab.ujumeonji.moco.model.auth.AuthService
import lab.ujumeonji.moco.model.user.io.AuthOutput
import lab.ujumeonji.moco.support.auth.RequiredAuth
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService,
) {
    @PostMapping("/signup")
    fun signup(
        @Valid @RequestBody request: SignUpRequest,
        bindingResult: BindingResult,
    ): ResponseEntity<AuthResponse> {
        if (bindingResult.hasErrors()) {
            val errorMessage = bindingResult.fieldErrors.joinToString(", ") { "${it.field}: ${it.defaultMessage}" }
            throw IllegalArgumentException(errorMessage)
        }

        val output = authService.signUp(request.toInput())
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(AuthResponse.from(output))
    }

    @PostMapping("/signin")
    fun login(
        @Valid @RequestBody request: SignInRequest,
        bindingResult: BindingResult,
    ): ResponseEntity<TokenResponse> {
        if (bindingResult.hasErrors()) {
            val errorMessage = bindingResult.fieldErrors.joinToString(", ") { "${it.field}: ${it.defaultMessage}" }
            throw IllegalArgumentException(errorMessage)
        }

        val output = authService.signIn(request.toInput())
        return ResponseEntity.ok(TokenResponse.from(output))
    }

    @GetMapping("/me")
    fun getMyProfile(
        @RequiredAuth userId: String,
    ): ResponseEntity<UserProfileResponse> {
        val output = authService.getMyProfile(userId)
        return ResponseEntity.ok(UserProfileResponse.from(output))
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
