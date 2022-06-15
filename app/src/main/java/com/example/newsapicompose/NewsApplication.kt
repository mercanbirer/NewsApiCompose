package com.example.newsapicompose

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class NewsApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        if (false) {
            Timber.plant(Timber.DebugTree())
        }
    }
}