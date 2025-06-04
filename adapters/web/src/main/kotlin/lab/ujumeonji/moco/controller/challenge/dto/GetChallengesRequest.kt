package lab.ujumeonji.moco.controller.challenge.dto

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

data class GetChallengesRequest(
    val title: String? = null,
    val difficulty: String? = null,
    val tag: String? = null,
    val page: Int = 0,
    val size: Int = 10,
    val sort: String = "createdAt,desc",
) {
    fun toPageable(): Pageable {
        val sortParams = sort.split(",")
        val direction =
            if (sortParams.size > 1 && sortParams[1].equals("asc", ignoreCase = true)) {
                Sort.Direction.ASC
            } else {
                Sort.Direction.DESC
            }
        val sortProperty = sortParams[0]
        return PageRequest.of(page, size, Sort.by(direction, sortProperty))
    }
}
