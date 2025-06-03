package lab.ujumeonji.moco.model

import lab.ujumeonji.moco.model.user.User
import org.springframework.stereotype.Component

@Component
class UserMapper {
    fun toDomain(entity: UserEntity): User {
        return User(
            id = entity.id,
            name = entity.name,
            email = entity.email,
            password = entity.password,
            authProvider = entity.authProvider,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
        )
    }

    fun toEntity(domain: User): UserEntity {
        return UserEntity(
            id = domain.id,
            name = domain.name,
            email = domain.email,
            password = domain.password,
            authProvider = domain.authProvider,
            createdAt = domain.createdAt,
            updatedAt = domain.updatedAt,
        )
    }
}
