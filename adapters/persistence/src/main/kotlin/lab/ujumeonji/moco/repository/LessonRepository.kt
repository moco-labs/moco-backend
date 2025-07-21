package lab.ujumeonji.moco.repository

import lab.ujumeonji.moco.model.LessonEntity
import lab.ujumeonji.moco.model.SectionTypeEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface LessonRepository : MongoRepository<LessonEntity, String> {
    fun findByChallengeId(challengeId: String): List<LessonEntity>

    fun findByChallengeId(
        challengeId: String,
        pageable: Pageable,
    ): Page<LessonEntity>

    fun findBySectionsType(type: SectionTypeEntity): List<LessonEntity>

    fun findBySectionsType(
        type: SectionTypeEntity,
        pageable: Pageable,
    ): Page<LessonEntity>

    fun findByChallengeIdAndSectionsType(
        challengeId: String,
        type: SectionTypeEntity,
    ): List<LessonEntity>

    fun findByChallengeIdAndSectionsType(
        challengeId: String,
        type: SectionTypeEntity,
        pageable: Pageable,
    ): Page<LessonEntity>
}
