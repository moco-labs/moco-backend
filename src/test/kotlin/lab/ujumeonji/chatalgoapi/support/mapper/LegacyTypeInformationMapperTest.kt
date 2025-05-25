package lab.ujumeonji.chatalgoapi.support.mapper

import lab.ujumeonji.chatalgoapi.model.Challenge
import lab.ujumeonji.chatalgoapi.model.ChatSession
import lab.ujumeonji.chatalgoapi.model.DailyChallenge
import lab.ujumeonji.chatalgoapi.model.Lesson
import lab.ujumeonji.chatalgoapi.model.User
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.springframework.data.mapping.Alias
import org.springframework.data.util.TypeInformation

class LegacyTypeInformationMapperTest {

    private val legacyTypeInformationMapper = LegacyTypeInformationMapper()

    @Test
    fun `유효한 레거시 클래스 이름에 대해 resolveTypeFrom은 올바른 TypeInformation을 반환해야 한다`() {
        // Given
        val validLegacyClassNames = mapOf(
            "lab.ujumeonji.chatalgoapi.model.ChatSession" to ChatSession::class.java,
            "lab.ujumeonji.chatalgoapi.model.User" to User::class.java,
            "lab.ujumeonji.chatalgoapi.model.Challenge" to Challenge::class.java,
            "lab.ujumeonji.chatalgoapi.model.DailyChallenge" to DailyChallenge::class.java,
            "lab.ujumeonji.chatalgoapi.model.Lesson" to Lesson::class.java
        )

        // When & Then
        validLegacyClassNames.forEach { (legacyClassName, expectedClass) ->
            val alias = Alias.of(legacyClassName)
            val typeInformation = legacyTypeInformationMapper.resolveTypeFrom(alias)

            assertEquals(expectedClass, typeInformation?.type)
        }
    }

    @Test
    fun `유효하지 않은 레거시 클래스 이름에 대해 resolveTypeFrom은 null을 반환해야 한다`() {
        // Given
        val invalidLegacyClassName = "lab.ujumeonji.chatalgoapi.model.NonExistentClass"
        val alias = Alias.of(invalidLegacyClassName)

        // When
        val typeInformation = legacyTypeInformationMapper.resolveTypeFrom(alias)

        // Then
        assertNull(typeInformation)
    }

    @Test
    fun `createAliasFor는 Alias NONE을 반환해야 한다`() {
        // Given
        val typeInformation = TypeInformation.of(ChatSession::class.java)

        // When
        val alias = legacyTypeInformationMapper.createAliasFor(typeInformation)

        // Then
        assertEquals(Alias.NONE, alias)
    }
}
