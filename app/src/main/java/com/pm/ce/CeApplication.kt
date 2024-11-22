package com.pm.ce

import android.app.Application
import com.pm.ce.presentation.screen.screensModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class CeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(androidContext = this@CeApplication)
            modules(listOf(screensModule()))
        }
    }
}