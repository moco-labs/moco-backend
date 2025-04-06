package lab.ujumeonji.chatalgoapi.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "algorithms")
data class Algorithm(
    @Id
    val id: String? = null,
    val name: String,
    val description: String,
    val category: String,
    val complexity: String,
    val implementations: List<String> = emptyList(),
    val tags: List<String> = emptyList()
)
