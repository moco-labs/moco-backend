package lab.ujumeonji.moco.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate

@Document(collection = "daily_challenges")
data class DailyChallengeEntity(
    @Id
    val id: String? = null,
    val challengeId: String,
    val date: LocalDate = LocalDate.now(),
    val isActive: Boolean = true,
)
