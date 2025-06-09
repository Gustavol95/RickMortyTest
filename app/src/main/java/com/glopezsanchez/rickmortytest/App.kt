package com.glopezsanchez.rickmortytest

import android.app.Application
import com.glopezsanchez.rickmortytest.di.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            androidLogger()
            modules(AppModule.appModule)
        }
    }
}