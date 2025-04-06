package lab.ujumeonji.chatalgoapi.repository

import lab.ujumeonji.chatalgoapi.model.Lesson
import lab.ujumeonji.chatalgoapi.model.SectionType
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface LessonRepository : MongoRepository<Lesson, String> {
    /**
     * 챌린지 ID로 레슨을 찾습니다.
     */
    fun findByChallengeId(challengeId: String): List<Lesson>

    /**
     * 레슨 섹션 유형으로 레슨을 찾습니다.
     */
    fun findBySectionsType(type: SectionType): List<Lesson>

    /**
     * 챌린지 ID와 섹션 유형으로 레슨을 찾습니다.
     */
    fun findByChallengeIdAndSectionsType(challengeId: String, type: SectionType): List<Lesson>
}
