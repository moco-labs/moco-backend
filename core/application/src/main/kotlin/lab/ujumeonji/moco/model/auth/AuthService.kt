package lab.ujumeonji.moco.model.auth

import lab.ujumeonji.moco.model.user.io.AuthOutput
import lab.ujumeonji.moco.model.user.io.SignInInput
import lab.ujumeonji.moco.model.user.io.SignUpInput
import lab.ujumeonji.moco.model.user.io.TokenOutput
import lab.ujumeonji.moco.model.user.io.UserProfileOutput

interface AuthService {
    fun signUp(input: SignUpInput): AuthOutput

    fun signIn(input: SignInInput): TokenOutput

    fun getMyProfile(userId: String): UserProfileOutput
}
