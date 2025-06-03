package lab.ujumeonji.moco.controller.auth.dto

import lab.ujumeonji.moco.model.user.io.UserProfileOutput

data class UserProfileResponse(
    val id: String,
    val name: String,
    val email: String,
) {
    companion object {
        fun from(output: UserProfileOutput): UserProfileResponse {
            return UserProfileResponse(
                id = output.id,
                name = output.name,
                email = output.email
            )
        }
    }
}