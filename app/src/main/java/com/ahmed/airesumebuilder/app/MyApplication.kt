package com.ahmed.airesumebuilder.app

import android.app.Application
import android.util.Log
import com.ahmed.airesumebuilder.BuildConfig
import com.google.firebase.Firebase
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.appCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.initialize
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.d("MyApplication", "Application onCreate started")
        if (BuildConfig.DEBUG) {
            FirebaseAppCheck.getInstance()
                .installAppCheckProviderFactory(
                    DebugAppCheckProviderFactory.getInstance()
                )
        }
        Log.d("MyApplication", "Firebase App Check initialized")
    }
}