package com.ahmed.airesumebuilder.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ahmed.airesumebuilder.data.local.dao.ResumeDao
import com.ahmed.airesumebuilder.data.local.dao.UsersDao
import com.ahmed.airesumebuilder.data.local.entity.ResumeEntity
import com.ahmed.airesumebuilder.data.local.entity.UserEntity

@Database(
    entities = [ResumeEntity::class, UserEntity::class],
    version = 1,
    exportSchema = false
)

@TypeConverters(Converters::class)
abstract class ResumeDatabase : RoomDatabase() {
    abstract fun resumeDao(): ResumeDao
    abstract fun userDao(): UsersDao

}

