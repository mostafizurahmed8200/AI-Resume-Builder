package com.ahmed.airesumebuilder.domain.usecase

import com.ahmed.airesumebuilder.data.repository.AuthRepository
import com.ahmed.airesumebuilder.domain.model.User
import com.ahmed.airesumebuilder.util.Resource
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AuthUseCases @Inject constructor(
    private val authRepository: AuthRepository
) {
    val currentUser: FirebaseUser? = authRepository.currentUser
    val isLoggedIn: Boolean = authRepository.isLoggedIn

    fun getAuthState(): Flow<FirebaseUser?> = authRepository.getAuthState()

    suspend fun login(email: String, password: String): Resource<User> =
        authRepository.signIn(email, password)


    suspend fun register(email: String, password: String, displayName: String): Resource<User> =
        authRepository.signUp(email, password, displayName)


    suspend fun logout() = authRepository.signOut()

    suspend fun resetPassword(email: String): Resource<Unit> = authRepository.resetPassword(email)

    fun getLocalUser(uid: String): Flow<User?> = authRepository.getLocalUser(uid)


}