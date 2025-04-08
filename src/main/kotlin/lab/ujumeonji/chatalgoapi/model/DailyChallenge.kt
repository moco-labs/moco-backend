package lab.ujumeonji.chatalgoapi.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate

@Document(collection = "daily_challenges")
data class DailyChallenge(
    @Id
    val id: String? = null,
    val challengeId: String,
    val date: LocalDate = LocalDate.now(),
    val isActive: Boolean = true
)
