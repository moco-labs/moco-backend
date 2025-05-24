package lab.ujumeonji.chatalgoapi.service

import lab.ujumeonji.chatalgoapi.dto.LoginRequest
import lab.ujumeonji.chatalgoapi.dto.SignupRequest
import lab.ujumeonji.chatalgoapi.exception.AuthenticationFailedException
import lab.ujumeonji.chatalgoapi.exception.EmailAlreadyExistsException
import lab.ujumeonji.chatalgoapi.model.User
import lab.ujumeonji.chatalgoapi.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    private val logger = LoggerFactory.getLogger(UserService::class.java)

    fun signUp(request: SignupRequest): User {
        if (userRepository.existsByEmail(request.email)) {
            throw EmailAlreadyExistsException("Email ${request.email} is already registered")
        }

        val newUser =
            User(
                id = UUID.randomUUID().toString(),
                name = request.name,
                email = request.email,
                password = passwordEncoder.encode(request.password),
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
            )

        return userRepository.save(newUser)
    }

    fun login(request: LoginRequest): User {
        val user =
            findByEmail(request.email)
                ?: throw AuthenticationFailedException("Invalid email or password")

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw AuthenticationFailedException("Invalid email or password")
        }

        return user
    }

    fun findByEmail(email: String): User? {
        return userRepository.findByEmail(email).orElse(null)
    }

    fun findById(id: String): User? {
        return userRepository.findById(id).orElse(null)
    }
}
