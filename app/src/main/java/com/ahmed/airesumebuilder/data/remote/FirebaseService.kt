package com.ahmed.airesumebuilder.data.remote

import com.ahmed.airesumebuilder.domain.model.Resume
import com.ahmed.airesumebuilder.domain.model.User
import com.ahmed.airesumebuilder.util.Constant
import com.ahmed.airesumebuilder.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseService @Inject constructor(
    private val auth: FirebaseAuth, private val fireStore: FirebaseFirestore
) {
    val signInErrorMsg = "Sign in failed"
    val signUpErrorMsg = "Sign up failed"
    val fetchDataErrorMsg = "Failed to fetch resumes"
    val resumeNotFoundError = "Resume not found"


    val currentUser: FirebaseUser?
        get() = auth.currentUser

    val isLoggedIn: Boolean
        get() = currentUser != null

    fun getAuthState(): Flow<FirebaseUser?> = callbackFlow {
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
            } ?: Resource.Error(message = signInErrorMsg)
        } catch (e: Exception) {
            Resource.Error(message = signInErrorMsg)
        }
    }

    suspend fun signUp(email: String, password: String, displayName: String): Resource<User> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            result.user?.let { firebaseUser ->
                val profileUpdates =
                    UserProfileChangeRequest.Builder().setDisplayName(displayName).build()

                firebaseUser.updateProfile(profileUpdates).await()

                val user = User(
                    uid = firebaseUser.uid,
                    email = email,
                    displayName = displayName,
                    createdAt = System.currentTimeMillis()

                )

                saveUserToFirestore(user)
                Resource.Success(user)
            } ?: Resource.Error(signUpErrorMsg)

        } catch (e: Exception) {
            Resource.Error(e.message ?: signUpErrorMsg)
        }
    }

    suspend fun signOut() {
        auth.signOut()
    }

    suspend fun resetPassword(email: String): Resource<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Password reset failed")
        }
    }


    suspend fun saveResume(resume: Resume): Resource<Unit> {
        return try {
            fireStore.collection(Constant.RESUMES_COLLECTION)
                .document(resume.id)
                .set(resume)
                .await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to save resume")
        }
    }



    suspend fun getResumes(userId: String): Resource<List<Resume>> {
        return try {
            val snapshot =
                fireStore.collection(Constant.RESUMES_COLLECTION).whereEqualTo("userId", userId)
                    .get().await()

            val resumes = snapshot.documents.mapNotNull { documentSnapshot ->
                documentSnapshot.toObject(Resume::class.java)
            }

            Resource.Success(resumes)
        } catch (e: Exception) {
            Resource.Error(e.message ?: fetchDataErrorMsg)
        }

    }

    suspend fun getResumeById(resumeId: String): Resource<Resume> {
        return try {
            val doc =
                fireStore.collection(Constant.RESUMES_COLLECTION).document(resumeId).get().await()

            doc.toObject(Resume::class.java)?.let {
                Resource.Success(it)

            } ?: Resource.Error(resumeNotFoundError)

        } catch (e: Exception) {
            Resource.Error(e.message ?: fetchDataErrorMsg)
        }
    }

    suspend fun deleteResume(resumeId: String): Resource<Unit> {
        return try {
            fireStore.collection(Constant.RESUMES_COLLECTION).document(resumeId).delete().await()
            Resource.Success(Unit)

        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to delete resume")
        }
    }

    fun getResumesFlow(userId: String): Flow<List<Resume>> = callbackFlow {
        val listener =
            fireStore.collection(Constant.RESUMES_COLLECTION).whereEqualTo("userId", userId)
                .addSnapshotListener { snapshots, exception ->
                    if (exception != null) {
                        close(exception)
                        return@addSnapshotListener
                    }

                    val resumes = snapshots?.documents?.mapNotNull { documentSnapshot ->
                        documentSnapshot.toObject(Resume::class.java)
                    } ?: emptyList()

                    trySend(resumes)


                }
        awaitClose { listener.remove() }

    }


    private suspend fun saveUserToFirestore(user: User) {
        fireStore.collection(Constant.USERS_COLLECTION).document(user.uid).set(user).await()
    }

    private suspend fun getUserFromFirestore(uid: String): User {
        val doc = fireStore.collection(Constant.USERS_COLLECTION).document(uid).get().await()

        return doc.toObject(User::class.java) ?: User(uid = uid)
    }

}