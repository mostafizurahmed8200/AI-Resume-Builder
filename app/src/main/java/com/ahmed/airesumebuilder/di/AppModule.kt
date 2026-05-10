package com.ahmed.airesumebuilder.di

import android.content.Context
import com.ahmed.airesumebuilder.util.PdfGenerator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providePdfGenerator(
        @ApplicationContext context: Context

    ): PdfGenerator = PdfGenerator(context)

}