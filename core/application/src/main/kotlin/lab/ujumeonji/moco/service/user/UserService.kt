package lab.ujumeonji.moco.service.user

import lab.ujumeonji.moco.adapter.UserRepositoryAdapter
import lab.ujumeonji.moco.model.User
import lab.ujumeonji.moco.service.user.exception.AuthenticationFailedException
import lab.ujumeonji.moco.service.user.exception.EmailAlreadyExistsException
import lab.ujumeonji.moco.service.user.io.SignInInput
import lab.ujumeonji.moco.service.user.io.SignUpInput
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

@Service
class UserService(
    private val userRepositoryAdapter: UserRepositoryAdapter,
    private val passwordEncoder: PasswordEncoder,
) {
    private val logger = LoggerFactory.getLogger(UserService::class.java)

    fun signUp(request: SignUpInput): User {
        if (userRepositoryAdapter.existsByEmail(request.email)) {
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

        return userRepositoryAdapter.save(newUser)
    }

    fun login(request: SignInInput): User {
        val user =
            findByEmail(request.email)
                ?: throw AuthenticationFailedException("Invalid email or password")

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw AuthenticationFailedException("Invalid email or password")
        }

        return user
    }

    fun findByEmail(email: String): User? {
        return userRepositoryAdapter.findByEmail(email).orElse(null)
    }

    fun findById(id: String): User? {
        return userRepositoryAdapter.findById(id).orElse(null)
    }
}
