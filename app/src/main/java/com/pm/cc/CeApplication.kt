package com.pm.cc

import android.app.Application
import com.pm.cc.data.di.DataModule
import com.pm.cc.di.Global
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class CeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(androidContext = this@CeApplication)
            modules(
                listOf(
                    DataModule.modules(),
                    Global.coroutineContextModule(),
                    Global.coroutineScopeModule(),
                    Global.dataSourceModule(),
                    Global.mapperModule(),
                    Global.useCaseModule(),
                    Global.screensModule(),
                    Global.dbModule()
                )
            )
        }
    }
}