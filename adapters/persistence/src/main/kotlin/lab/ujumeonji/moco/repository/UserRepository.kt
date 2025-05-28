package lab.ujumeonji.moco.repository

import lab.ujumeonji.moco.model.UserEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface UserRepository : MongoRepository<UserEntity, String> {
    fun findByEmail(email: String): Optional<UserEntity>

    fun existsByEmail(email: String): Boolean
}
