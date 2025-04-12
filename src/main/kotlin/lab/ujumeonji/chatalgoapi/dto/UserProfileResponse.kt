package lab.ujumeonji.chatalgoapi.dto

import java.time.LocalDateTime

/**
 * 사용자 프로필 정보 응답을 위한 DTO
 */
data class UserProfileResponse(
    val id: String,
    val name: String,
    val email: String,
)
