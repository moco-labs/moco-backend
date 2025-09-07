package lab.ujumeonji.moco.model.user

import lab.ujumeonji.moco.adapter.UserRepositoryAdapter
import lab.ujumeonji.moco.model.user.io.SignInInput
import lab.ujumeonji.moco.model.user.io.SignUpInput
import lab.ujumeonji.moco.model.user.io.UserOutput
import lab.ujumeonji.moco.support.error.BusinessException
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepositoryAdapter: UserRepositoryAdapter,
    private val passwordEncoder: PasswordEncoder,
) {
    private val logger = LoggerFactory.getLogger(UserService::class.java)

    fun signUp(request: SignUpInput): UserOutput {
        if (userRepositoryAdapter.existsByEmail(request.email)) {
            throw BusinessException.emailAlreadyExists(request.email)
        }

        val createdUser =
            userRepositoryAdapter.save(
                User.signUp(
                    name = request.name,
                    email = request.email,
                    password = passwordEncoder.encode(request.password),
                ),
            )

        return UserOutput.fromDomain(createdUser)
    }

    fun login(request: SignInInput): UserOutput {
        val user =
            findByEmail(request.email)
                ?: throw BusinessException.authenticationFailed("Invalid email or password")

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw BusinessException.authenticationFailed("Invalid email or password")
        }

        return UserOutput.fromDomain(user)
    }

    private fun findByEmail(email: String): User? {
        return userRepositoryAdapter.findByEmail(email)
    }

    fun findById(id: String): User? {
        return userRepositoryAdapter.findById(id)
    }
}
