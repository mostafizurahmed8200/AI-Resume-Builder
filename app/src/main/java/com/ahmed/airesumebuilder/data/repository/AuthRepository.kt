package com.ahmed.airesumebuilder.data.repository

import com.ahmed.airesumebuilder.data.local.dao.UsersDao
import com.ahmed.airesumebuilder.data.local.entity.UserEntity
import com.ahmed.airesumebuilder.data.remote.FirebaseService
import com.ahmed.airesumebuilder.domain.model.User
import com.ahmed.airesumebuilder.util.Resource
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val firebaseService: FirebaseService, private val userDao: UsersDao
) {
    val currentUser: FirebaseUser?
        get() = firebaseService.currentUser

    val isLoggedIn: Boolean
        get() = firebaseService.isLoggedIn


    fun getAuthState(): Flow<FirebaseUser?> = firebaseService.getAuthState()

    //Sign-in firebase data to store in database
    suspend fun signIn(email: String, password: String): Resource<User> {
        val result = firebaseService.signIn(email, password)
        if (result is Resource.Success) {
            result.data?.let { user ->
                userDao.insertUser(UserEntity.fromDomain(user))
            }
        }

        return result
    }

    //Signup firebase data to store in database
    suspend fun signUp(email: String, password: String, displayMessage: String): Resource<User> {
        val result = firebaseService.signUp(email, password, displayMessage)

        if (result is Resource.Success) {
            result.data?.let { user ->
                userDao.insertUser(UserEntity.fromDomain(user))
            }
        }

        return result
    }

    suspend fun signOut() {
        currentUser?.uid?.let { uid ->
            userDao.deleteUser(uid)
        }
        firebaseService.signOut()
    }

    suspend fun resetPassword(email: String): Resource<Unit> {
        return firebaseService.resetPassword(email)
    }

    fun getLocalUser(uid: String): Flow<User?> {
        return userDao.getUsersById(uid).map { it?.toDomain() }
    }

}
