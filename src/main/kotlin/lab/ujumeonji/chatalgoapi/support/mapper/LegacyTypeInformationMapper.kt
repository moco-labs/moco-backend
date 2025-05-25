package lab.ujumeonji.chatalgoapi.support.mapper

import lab.ujumeonji.chatalgoapi.model.Challenge
import lab.ujumeonji.chatalgoapi.model.ChatSession
import lab.ujumeonji.chatalgoapi.model.DailyChallenge
import lab.ujumeonji.chatalgoapi.model.Lesson
import lab.ujumeonji.chatalgoapi.model.User
import org.springframework.data.convert.TypeInformationMapper
import org.springframework.data.mapping.Alias
import org.springframework.data.util.TypeInformation

class LegacyTypeInformationMapper : TypeInformationMapper {
    private val legacyClassToNewClassMap: Map<String, Class<*>> =
        mapOf(
            "lab.ujumeonji.chatalgoapi.model.ChatSession" to ChatSession::class.java,
            "lab.ujumeonji.chatalgoapi.model.User" to User::class.java,
            "lab.ujumeonji.chatalgoapi.model.Challenge" to Challenge::class.java,
            "lab.ujumeonji.chatalgoapi.model.DailyChallenge" to DailyChallenge::class.java,
            "lab.ujumeonji.chatalgoapi.model.Lesson" to Lesson::class.java,
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
