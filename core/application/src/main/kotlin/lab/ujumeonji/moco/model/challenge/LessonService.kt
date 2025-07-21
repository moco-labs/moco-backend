package lab.ujumeonji.moco.model.challenge

import lab.ujumeonji.moco.adapter.LessonRepositoryAdapter
import lab.ujumeonji.moco.model.challenge.io.CreateLessonInput
import lab.ujumeonji.moco.model.challenge.io.LessonOutput
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class LessonService(
    private val lessonRepositoryAdapter: LessonRepositoryAdapter,
    private val challengeService: ChallengeService,
) {
    private val logger = LoggerFactory.getLogger(LessonService::class.java)

    fun findAll(pageable: Pageable): Page<LessonOutput> = lessonRepositoryAdapter.findAll(pageable).map(LessonOutput::fromDomain)

    fun findByChallengeId(
        challengeId: String,
        pageable: Pageable,
    ): Page<LessonOutput> = lessonRepositoryAdapter.findByChallengeId(challengeId, pageable).map(LessonOutput::fromDomain)

    fun findBySectionType(
        type: SectionType,
        pageable: Pageable,
    ): Page<LessonOutput> = lessonRepositoryAdapter.findBySectionsType(type, pageable).map(LessonOutput::fromDomain)

    fun findByChallengeIdAndSectionType(
        challengeId: String,
        type: SectionType,
        pageable: Pageable,
    ): Page<LessonOutput> =
        lessonRepositoryAdapter.findByChallengeIdAndSectionsType(challengeId, type, pageable).map(
            LessonOutput::fromDomain,
        )

    fun createLesson(input: CreateLessonInput): LessonOutput {
        val lesson = save(input)
        return LessonOutput.fromDomain(lesson)
    }

    private fun save(input: CreateLessonInput): Lesson {
        val challenge = challengeService.findById(input.challengeId)
        if (challenge == null) {
            throw IllegalArgumentException("Challenge with ID ${input.challengeId} does not exist")
        }

        logger.info("Saving lesson: Challenge ID = {}", input.challengeId)
        return lessonRepositoryAdapter.save(input.toDomain())
    }
}
