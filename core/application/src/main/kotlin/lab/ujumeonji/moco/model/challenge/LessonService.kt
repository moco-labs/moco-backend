package lab.ujumeonji.moco.model.challenge

import lab.ujumeonji.moco.adapter.LessonRepositoryAdapter
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class LessonService(
    private val lessonRepositoryAdapter: LessonRepositoryAdapter,
    private val challengeService: ChallengeService,
) {
    private val logger = LoggerFactory.getLogger(LessonService::class.java)

    fun findAll(): List<Lesson> = lessonRepositoryAdapter.findAll()

    fun findByChallengeId(challengeId: String): List<Lesson> = lessonRepositoryAdapter.findByChallengeId(challengeId)

    fun findBySectionType(type: SectionType): List<Lesson> = lessonRepositoryAdapter.findBySectionsType(type)

    fun findByChallengeIdAndSectionType(
        challengeId: String,
        type: SectionType,
    ): List<Lesson> = lessonRepositoryAdapter.findByChallengeIdAndSectionsType(challengeId, type)

    fun save(lesson: Lesson): Lesson {
        val challenge = challengeService.findById(lesson.challengeId)
        if (challenge == null) {
            throw IllegalArgumentException("Challenge with ID ${lesson.challengeId} does not exist")
        }

        logger.info("레슨 저장 중: 챌린지 ID = {}", lesson.challengeId)
        return lessonRepositoryAdapter.save(lesson)
    }
}
