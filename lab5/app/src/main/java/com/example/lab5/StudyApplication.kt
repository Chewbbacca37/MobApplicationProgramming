package com.example.lab5

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class StudyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@StudyApplication)
            modules(appModule)
        }
    }
}