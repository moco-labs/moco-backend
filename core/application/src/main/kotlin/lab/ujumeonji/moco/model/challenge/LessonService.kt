package lab.ujumeonji.moco.model.challenge

import lab.ujumeonji.moco.adapter.LessonRepositoryAdapter
import lab.ujumeonji.moco.model.challenge.io.CreateLessonInput
import lab.ujumeonji.moco.model.challenge.io.LessonOutput
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class LessonService(
    private val lessonRepositoryAdapter: LessonRepositoryAdapter,
    private val challengeService: ChallengeService,
) {
    private val logger = LoggerFactory.getLogger(LessonService::class.java)

    fun findAll(): List<Lesson> = lessonRepositoryAdapter.findAll()

    fun findAllOutput(): List<LessonOutput> = findAll().map { LessonOutput.fromDomain(it) }

    fun findAll(pageable: Pageable): Page<Lesson> {
        val lessons = findAll()
        val start = pageable.offset.toInt()
        val end = (start + pageable.pageSize).coerceAtMost(lessons.size)
        val pageContent = if (start < end) lessons.subList(start, end) else emptyList()
        return PageImpl(pageContent, pageable, lessons.size.toLong())
    }

    fun findAllOutput(pageable: Pageable): Page<LessonOutput> {
        val lessonPage = findAll(pageable)
        return PageImpl(
            lessonPage.content.map { LessonOutput.fromDomain(it) },
            lessonPage.pageable,
            lessonPage.totalElements
        )
    }

    fun findByChallengeId(challengeId: String): List<Lesson> = lessonRepositoryAdapter.findByChallengeId(challengeId)

    fun findByChallengeIdOutput(challengeId: String): List<LessonOutput> = 
        findByChallengeId(challengeId).map { LessonOutput.fromDomain(it) }

    fun findByChallengeId(challengeId: String, pageable: Pageable): Page<Lesson> {
        val lessons = findByChallengeId(challengeId)
        val start = pageable.offset.toInt()
        val end = (start + pageable.pageSize).coerceAtMost(lessons.size)
        val pageContent = if (start < end) lessons.subList(start, end) else emptyList()
        return PageImpl(pageContent, pageable, lessons.size.toLong())
    }

    fun findByChallengeIdOutput(challengeId: String, pageable: Pageable): Page<LessonOutput> {
        val lessonPage = findByChallengeId(challengeId, pageable)
        return PageImpl(
            lessonPage.content.map { LessonOutput.fromDomain(it) },
            lessonPage.pageable,
            lessonPage.totalElements
        )
    }

    fun findBySectionType(type: SectionType): List<Lesson> = lessonRepositoryAdapter.findBySectionsType(type)

    fun findBySectionTypeOutput(type: SectionType): List<LessonOutput> = 
        findBySectionType(type).map { LessonOutput.fromDomain(it) }

    fun findBySectionType(type: SectionType, pageable: Pageable): Page<Lesson> {
        val lessons = findBySectionType(type)
        val start = pageable.offset.toInt()
        val end = (start + pageable.pageSize).coerceAtMost(lessons.size)
        val pageContent = if (start < end) lessons.subList(start, end) else emptyList()
        return PageImpl(pageContent, pageable, lessons.size.toLong())
    }

    fun findBySectionTypeOutput(type: SectionType, pageable: Pageable): Page<LessonOutput> {
        val lessonPage = findBySectionType(type, pageable)
        return PageImpl(
            lessonPage.content.map { LessonOutput.fromDomain(it) },
            lessonPage.pageable,
            lessonPage.totalElements
        )
    }

    fun findByChallengeIdAndSectionType(
        challengeId: String,
        type: SectionType,
    ): List<Lesson> = lessonRepositoryAdapter.findByChallengeIdAndSectionsType(challengeId, type)

    fun findByChallengeIdAndSectionTypeOutput(
        challengeId: String,
        type: SectionType,
    ): List<LessonOutput> = findByChallengeIdAndSectionType(challengeId, type).map { LessonOutput.fromDomain(it) }

    fun findByChallengeIdAndSectionType(
        challengeId: String,
        type: SectionType,
        pageable: Pageable
    ): Page<Lesson> {
        val lessons = findByChallengeIdAndSectionType(challengeId, type)
        val start = pageable.offset.toInt()
        val end = (start + pageable.pageSize).coerceAtMost(lessons.size)
        val pageContent = if (start < end) lessons.subList(start, end) else emptyList()
        return PageImpl(pageContent, pageable, lessons.size.toLong())
    }

    fun findByChallengeIdAndSectionTypeOutput(
        challengeId: String,
        type: SectionType,
        pageable: Pageable
    ): Page<LessonOutput> {
        val lessonPage = findByChallengeIdAndSectionType(challengeId, type, pageable)
        return PageImpl(
            lessonPage.content.map { LessonOutput.fromDomain(it) },
            lessonPage.pageable,
            lessonPage.totalElements
        )
    }

    fun save(input: CreateLessonInput): Lesson {
        val challenge = challengeService.findById(input.challengeId)
        if (challenge == null) {
            throw IllegalArgumentException("Challenge with ID ${input.challengeId} does not exist")
        }

        logger.info("Saving lesson: Challenge ID = {}", input.challengeId)
        return lessonRepositoryAdapter.save(input.toDomain())
    }

    fun save(lesson: Lesson): Lesson {
        val challenge = challengeService.findById(lesson.challengeId)
        if (challenge == null) {
            throw IllegalArgumentException("Challenge with ID ${lesson.challengeId} does not exist")
        }

        logger.info("Saving lesson: Challenge ID = {}", lesson.challengeId)
        return lessonRepositoryAdapter.save(lesson)
    }

    fun saveOutput(input: CreateLessonInput): LessonOutput {
        val lesson = save(input)
        return LessonOutput.fromDomain(lesson)
    }
}
