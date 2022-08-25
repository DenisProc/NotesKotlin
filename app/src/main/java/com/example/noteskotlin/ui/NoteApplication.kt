package com.example.noteskotlin.ui

import android.app.Application
import com.example.noteskotlin.model.AppDI
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class NoteApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@NoteApplication)

            modules(
                AppDI().koinModule
            )
        }
    }
}