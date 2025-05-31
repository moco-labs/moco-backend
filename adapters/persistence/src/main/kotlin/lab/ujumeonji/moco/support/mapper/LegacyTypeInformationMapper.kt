package lab.ujumeonji.moco.support.mapper

import lab.ujumeonji.moco.model.ChallengeEntity
import lab.ujumeonji.moco.model.ChatSessionEntity
import lab.ujumeonji.moco.model.DailyChallengeEntity
import lab.ujumeonji.moco.model.LessonEntity
import lab.ujumeonji.moco.model.UserEntity
import org.springframework.data.convert.TypeInformationMapper
import org.springframework.data.mapping.Alias
import org.springframework.data.util.TypeInformation

class LegacyTypeInformationMapper : TypeInformationMapper {
    private val legacyClassToNewClassMap: Map<String, Class<*>> =
        mapOf(
            "lab.ujumeonji.chatalgoapi.model.ChatSession" to ChatSessionEntity::class.java,
            "lab.ujumeonji.chatalgoapi.model.User" to UserEntity::class.java,
            "lab.ujumeonji.chatalgoapi.model.Challenge" to ChallengeEntity::class.java,
            "lab.ujumeonji.chatalgoapi.model.DailyChallenge" to DailyChallengeEntity::class.java,
            "lab.ujumeonji.chatalgoapi.model.Lesson" to LessonEntity::class.java,
        )

    override fun resolveTypeFrom(alias: Alias): TypeInformation<*>? {
        val aliasString = alias.value.toString()
        val targetClass = legacyClassToNewClassMap[aliasString]

        return targetClass?.let { TypeInformation.of(it) }
    }

    override fun createAliasFor(type: TypeInformation<*>): Alias {
        return Alias.NONE
    }
}
