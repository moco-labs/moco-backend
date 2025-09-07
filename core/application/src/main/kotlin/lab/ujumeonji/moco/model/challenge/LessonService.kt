package lab.ujumeonji.moco.model.challenge

import lab.ujumeonji.moco.adapter.LessonRepositoryAdapter
import lab.ujumeonji.moco.model.challenge.io.CreateLessonInput
import lab.ujumeonji.moco.model.challenge.io.LessonOutput
import lab.ujumeonji.moco.support.error.BusinessException
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

    fun searchLessons(
        challengeId: String?,
        sectionType: String?,
        pageable: Pageable,
    ): Page<LessonOutput> {
        return when {
            challengeId != null && sectionType != null -> {
                val type = parseSectionType(sectionType)
                findByChallengeIdAndSectionType(challengeId, type, pageable)
            }
            challengeId != null -> {
                findByChallengeId(challengeId, pageable)
            }
            sectionType != null -> {
                val type = parseSectionType(sectionType)
                findBySectionType(type, pageable)
            }
            else -> {
                findAll(pageable)
            }
        }
    }

    fun createLesson(input: CreateLessonInput): LessonOutput {
        val lesson = save(input)
        return LessonOutput.fromDomain(lesson)
    }

    private fun findAll(pageable: Pageable): Page<LessonOutput> = lessonRepositoryAdapter.findAll(pageable).map(LessonOutput::fromDomain)

    private fun findByChallengeId(
        challengeId: String,
        pageable: Pageable,
    ): Page<LessonOutput> = lessonRepositoryAdapter.findByChallengeId(challengeId, pageable).map(LessonOutput::fromDomain)

    private fun findBySectionType(
        type: SectionType,
        pageable: Pageable,
    ): Page<LessonOutput> = lessonRepositoryAdapter.findBySectionsType(type, pageable).map(LessonOutput::fromDomain)

    private fun findByChallengeIdAndSectionType(
        challengeId: String,
        type: SectionType,
        pageable: Pageable,
    ): Page<LessonOutput> =
        lessonRepositoryAdapter.findByChallengeIdAndSectionsType(challengeId, type, pageable).map(
            LessonOutput::fromDomain,
        )

    private fun parseSectionType(sectionType: String): SectionType {
        return try {
            SectionType.valueOf(sectionType.uppercase())
        } catch (e: IllegalArgumentException) {
            throw BusinessException.invalidRequest("유효하지 않은 섹션 타입입니다: $sectionType")
        }
    }

    private fun save(input: CreateLessonInput): Lesson {
        val challenge = challengeService.findById(input.challengeId)
        if (challenge == null) {
            throw BusinessException.challengeNotFound(input.challengeId)
        }

        logger.info("Saving lesson: Challenge ID = {}", input.challengeId)
        return lessonRepositoryAdapter.save(input.toDomain())
    }
}
