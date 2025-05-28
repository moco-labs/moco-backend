package lab.ujumeonji.moco.repository

import lab.ujumeonji.moco.model.LessonEntity
import lab.ujumeonji.moco.model.SectionTypeEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface LessonRepository : MongoRepository<LessonEntity, String> {
    fun findByChallengeId(challengeId: String): List<LessonEntity>

    fun findBySectionsType(type: SectionTypeEntity): List<LessonEntity>

    fun findByChallengeIdAndSectionsType(
        challengeId: String,
        type: SectionTypeEntity,
    ): List<LessonEntity>
}
