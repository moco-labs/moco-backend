package lab.ujumeonji.moco.adapter

import lab.ujumeonji.moco.model.UserMapper
import lab.ujumeonji.moco.model.user.User
import lab.ujumeonji.moco.repository.UserRepository
import org.springframework.stereotype.Component
import kotlin.jvm.optionals.getOrNull

@Component
class UserRepositoryAdapter(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper,
) {
    fun findAll(): List<User> {
        return userRepository.findAll().map { userMapper.toDomain(it) }
    }

    fun findById(id: String): User? {
        return userRepository.findById(id).map { userMapper.toDomain(it) }.getOrNull()
    }

    fun findByEmail(email: String): User? {
        return userRepository.findByEmail(email).map { userMapper.toDomain(it) }.getOrNull()
    }

    fun existsByEmail(email: String): Boolean {
        return userRepository.existsByEmail(email)
    }

    fun save(user: User): User {
        val entity = userMapper.toEntity(user)
        val savedEntity = userRepository.save(entity)
        return userMapper.toDomain(savedEntity)
    }

    fun deleteById(id: String) {
        userRepository.deleteById(id)
    }
}
