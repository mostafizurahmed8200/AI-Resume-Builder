package com.ahmed.airesumebuilder.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.ahmed.airesumebuilder.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UsersDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("select * from users where uid=:uid")
    fun getUsersById(uid: String): Flow<UserEntity?>


    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("delete from users where uid=:uid")
    suspend fun deleteUser(uid: String)


}