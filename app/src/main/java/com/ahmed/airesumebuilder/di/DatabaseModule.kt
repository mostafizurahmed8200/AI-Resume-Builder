package com.ahmed.airesumebuilder.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ahmed.airesumebuilder.data.local.ResumeDatabase
import com.ahmed.airesumebuilder.data.local.dao.ResumeDao
import com.ahmed.airesumebuilder.data.local.dao.UsersDao
import com.ahmed.airesumebuilder.util.Constant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideResumeDatabase(@ApplicationContext context: Context): ResumeDatabase {
        return Room.databaseBuilder(
            context,
            ResumeDatabase::class.java,
            Constant.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideResumeDao(database: ResumeDatabase): ResumeDao {
        return database.resumeDao()
    }


    @Provides
    @Singleton
    fun provideUserDao(database: ResumeDatabase): UsersDao {
        return database.userDao()
    }

}