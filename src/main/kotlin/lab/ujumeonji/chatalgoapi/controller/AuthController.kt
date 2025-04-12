package lab.ujumeonji.chatalgoapi.controller

import lab.ujumeonji.chatalgoapi.dto.AuthResponse
import lab.ujumeonji.chatalgoapi.dto.SignupRequest
import lab.ujumeonji.chatalgoapi.exception.EmailAlreadyExistsException
import lab.ujumeonji.chatalgoapi.exception.PasswordMismatchException
import lab.ujumeonji.chatalgoapi.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/auth")
class AuthController(private val userService: UserService) {
    private val logger = LoggerFactory.getLogger(AuthController::class.java)

    /**
     * 이메일과 비밀번호를 사용한 회원가입 API
     */
    @PostMapping("/signup")
    fun signup(@Valid @RequestBody request: SignupRequest, bindingResult: BindingResult): ResponseEntity<AuthResponse> {
        // 유효성 검증 실패 처리
        if (bindingResult.hasErrors()) {
            val errorMessage = bindingResult.fieldErrors.joinToString(", ") { "${it.field}: ${it.defaultMessage}" }
            return ResponseEntity
                .badRequest()
                .body(AuthResponse(false, errorMessage))
        }

        try {
            // 회원가입 처리
            val user = userService.signUp(request)

            // 성공 응답
            return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                    AuthResponse(
                        success = true,
                        message = "Registration successful",
                        userId = user.id,
                        email = user.email,
                        name = user.name
                    )
                )
        } catch (e: EmailAlreadyExistsException) {
            // 이메일 중복 예외 처리
            return ResponseEntity
                .badRequest()
                .body(AuthResponse(false, e.message ?: "Email already exists"))
        } catch (e: PasswordMismatchException) {
            // 비밀번호 불일치 예외 처리
            return ResponseEntity
                .badRequest()
                .body(AuthResponse(false, e.message ?: "Password and confirmation don't match"))
        } catch (e: Exception) {
            // 기타 예외 처리
            logger.error("Error during user registration", e)
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(AuthResponse(false, "An unexpected error occurred"))
        }
    }

    /**
     * 소셜 로그인 회원가입 API (Google)
     * 실제 구현은 OAuth2 클라이언트 설정과 함께 추가 개발이 필요합니다.
     */
    @PostMapping("/signup/google")
    fun signupWithGoogle(): ResponseEntity<AuthResponse> {
        // TODO: Google OAuth 구현
        return ResponseEntity
            .status(HttpStatus.NOT_IMPLEMENTED)
            .body(AuthResponse(false, "Google signup not yet implemented"))
    }

    /**
     * 소셜 로그인 회원가입 API (Facebook)
     * 실제 구현은 OAuth2 클라이언트 설정과 함께 추가 개발이 필요합니다.
     */
    @PostMapping("/signup/facebook")
    fun signupWithFacebook(): ResponseEntity<AuthResponse> {
        // TODO: Facebook OAuth 구현
        return ResponseEntity
            .status(HttpStatus.NOT_IMPLEMENTED)
            .body(AuthResponse(false, "Facebook signup not yet implemented"))
    }
}
