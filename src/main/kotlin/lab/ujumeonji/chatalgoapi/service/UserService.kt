package lab.ujumeonji.chatalgoapi.service

import lab.ujumeonji.chatalgoapi.dto.SignupRequest
import lab.ujumeonji.chatalgoapi.exception.EmailAlreadyExistsException
import lab.ujumeonji.chatalgoapi.exception.PasswordMismatchException
import lab.ujumeonji.chatalgoapi.model.User
import lab.ujumeonji.chatalgoapi.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    private val logger = LoggerFactory.getLogger(UserService::class.java)

    /**
     * 일반 이메일/비밀번호 방식으로 회원가입합니다.
     */
    fun signUp(request: SignupRequest): User {
        // 이메일 중복 확인
        if (userRepository.existsByEmail(request.email)) {
            throw EmailAlreadyExistsException("Email ${request.email} is already registered")
        }

        // 비밀번호 일치 확인
        if (request.password != request.confirmPassword) {
            throw PasswordMismatchException("Password and confirmation do not match")
        }

        // 새 사용자 생성
        val newUser = User(
            id = UUID.randomUUID().toString(),
            name = request.name,
            email = request.email,
            password = passwordEncoder.encode(request.password),
            agreedToTerms = request.agreedToTerms,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        // DB에 저장 및 반환
        return userRepository.save(newUser)
    }

    /**
     * 이메일로 사용자를 찾습니다.
     */
    fun findByEmail(email: String): User? {
        return userRepository.findByEmail(email).orElse(null)
    }

    /**
     * ID로 사용자를 찾습니다.
     */
    fun findById(id: String): User? {
        return userRepository.findById(id).orElse(null)
    }
}
