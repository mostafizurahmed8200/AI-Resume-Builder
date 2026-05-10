package com.ahmed.airesumebuilder.di

import com.ahmed.airesumebuilder.data.remote.FirebaseService
import com.ahmed.airesumebuilder.data.remote.GeminiService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()


    @Singleton
    @Provides
    fun provideFirebase(): FirebaseFirestore = FirebaseFirestore.getInstance()


    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()

    @Singleton
    @Provides
    fun provideFirebaseService(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): FirebaseService = FirebaseService(auth, firestore)


    @Singleton
    @Provides
    fun provideGeminiService(): GeminiService = GeminiService()

}