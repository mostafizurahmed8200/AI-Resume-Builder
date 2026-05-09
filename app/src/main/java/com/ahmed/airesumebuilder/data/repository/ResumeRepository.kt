package com.ahmed.airesumebuilder.data.repository

import com.ahmed.airesumebuilder.data.local.dao.ResumeDao
import com.ahmed.airesumebuilder.data.local.entity.ResumeEntity
import com.ahmed.airesumebuilder.data.remote.FirebaseService
import com.ahmed.airesumebuilder.domain.model.Resume
import com.ahmed.airesumebuilder.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResumeRepository @Inject constructor(
    private val resumeDao: ResumeDao, private val firebaseService: FirebaseService
) {

    fun getLocalResumes(userId: String): Flow<List<Resume>> {
        return resumeDao.getResumesByUser(userId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    fun getResumeById(resumeId: String): Flow<Resume?> {
        return resumeDao.getResumeByIdFlow(resumeId).map { it?.toDomain() }
    }

    suspend fun getResumeByIdOnce(resumeId: String): Resume? {
        return resumeDao.getResumeById(resumeId)?.toDomain()
    }

    suspend fun saveResume(resume: Resume, syncToCloud: Boolean = true): Resource<Unit> {
        return try {
            resumeDao.insertResumes(
                ResumeEntity.fromDomain(
                    resume.copy(
                        updatedAt = System.currentTimeMillis(),
                        isSynced = false
                    )
                )
            )

            if (syncToCloud) {
                val cloudResult = firebaseService.saveResume(resume)

                if (cloudResult is Resource.Success) {
                    resumeDao
                        .markAsSynced(resume.id)
                }
            }
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to save resume")
        }

    }

    suspend fun deleteResume(resumeId: String): Resource<Unit> {
        return try {
            resumeDao.deleteResumsById(resumeId)
            firebaseService.deleteResume(resumeId)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to delete resume")
        }


    }

    suspend fun syncResumes(userId: String): Resource<Unit> {
        return try {
            val unsyncedResumes = resumeDao.getUnsyncedResumes()
            unsyncedResumes.forEach { entity ->
                val cloudResult = firebaseService.saveResume(entity.toDomain())

                if (cloudResult is Resource.Success) {
                    resumeDao.markAsSynced(entity.id)
                }
            }
            val cloudResumes = firebaseService.getResumes(userId)
            if (cloudResumes is Resource.Success) {
                cloudResumes.data?.forEach { resume ->
                    resumeDao.insertResumes(
                        ResumeEntity.fromDomain(
                            resume.copy(
                                isSynced = true
                            )
                        )
                    )
                }
            }

            Resource.Success(Unit)

        } catch (e: Exception) {
            Resource.Error(e.message ?: "Sync failed")
        }
    }

}