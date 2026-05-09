package com.ahmed.airesumebuilder.data.remote

import com.ahmed.airesumebuilder.domain.model.User
import com.ahmed.airesumebuilder.util.Constant
import com.ahmed.airesumebuilder.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseService @Inject constructor(
    private val auth: FirebaseAuth,
    private val fireStore: FirebaseFirestore
) {
    val currentUser: FirebaseUser?
        get() = auth.currentUser

    val isLoggedIn: Boolean
        get() = currentUser != null

    fun getAuthState(): Flow<FirebaseUser?> =
        callbackFlow {
            val listener = FirebaseAuth.AuthStateListener { auth ->
                trySend(auth.currentUser)
            }
            auth.addAuthStateListener(listener)
            awaitClose { auth.removeAuthStateListener(listener) }
        }


    suspend fun signIn(email: String, password: String): Resource<User> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            result.user?.let { firebaseUser ->
                val user = getUserFromFirestore(firebaseUser.uid)
                Resource.Success(user)
            } ?: Resource.Error(message = "Sign in failed")
        } catch (e: Exception) {
            Resource.Error(message = "Sign in failed")
        }
    }






    private suspend fun saveUserToFirestore(user: User) {
        fireStore.collection(Constant.USERS_COLLECTION)
            .document(user.uid)
            .set(user)
            .await()
    }

    private suspend fun getUserFromFirestore(uid: String): User {
        val doc = fireStore.collection(Constant.USERS_COLLECTION)
            .document(uid)
            .get()
            .await()

        return doc.toObject(User::class.java) ?: User(uid = uid)
    }

}