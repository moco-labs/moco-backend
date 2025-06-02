package lab.ujumeonji.moco.adapter

import lab.ujumeonji.moco.model.UserMapper
import lab.ujumeonji.moco.model.user.User
import lab.ujumeonji.moco.repository.UserRepository
import org.springframework.stereotype.Component
import java.util.Optional

@Component
class UserRepositoryAdapter(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper,
) {
    fun findAll(): List<User> {
        return userRepository.findAll().map { userMapper.toDomain(it) }
    }

    fun findById(id: String): Optional<User> {
        return userRepository.findById(id).map { userMapper.toDomain(it) }
    }

    fun findByEmail(email: String): Optional<User> {
        return userRepository.findByEmail(email).map { userMapper.toDomain(it) }
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
