package lab.ujumeonji.chatalgoapi.repository

import lab.ujumeonji.chatalgoapi.model.Lesson
import lab.ujumeonji.chatalgoapi.model.SectionType
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface LessonRepository : MongoRepository<Lesson, String> {
    fun findByChallengeId(challengeId: String): List<Lesson>

    fun findBySectionsType(type: SectionType): List<Lesson>

    fun findByChallengeIdAndSectionsType(
        challengeId: String,
        type: SectionType,
    ): List<Lesson>
}
