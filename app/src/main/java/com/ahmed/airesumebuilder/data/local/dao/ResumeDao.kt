package com.ahmed.airesumebuilder.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ahmed.airesumebuilder.data.local.entity.ResumeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ResumeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResumes(resume: ResumeEntity)

    @Query("Select * from resumes where userId=:userId order by updatedAt Desc")
    fun getResumesByUser(userId: String): Flow<List<ResumeEntity>>

    @Query("Select * from resumes where id=:resumeId")
    suspend fun getResumeById(resumeId: String): ResumeEntity?

    @Update
    suspend fun updateResumes(resume: ResumeEntity)

    @Delete
    suspend fun deleteResumes(resume: ResumeEntity)

    @Query("delete from resumes where id=:resumeId")
    suspend fun deleteResumsById(resumeId: String)


    @Query("SELECT * FROM resumes WHERE isSynced = 0")
    suspend fun getUnsyncedResumes(): List<ResumeEntity>

    @Query("update resumes set isSynced=1 where id=:resumeId")
    suspend fun markAsSynced(resumeId: String)
}