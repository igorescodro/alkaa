package com.escodro.alkaa

import android.app.Application
import android.content.Context
import com.escodro.shared.di.initKoin
import logcat.AndroidLogcatLogger
import logcat.LogPriority.VERBOSE
import org.koin.dsl.module

/**
 * Alkaa [Application] class.
 */
class AlkaaApp : Application() {

    override fun onCreate() {
        super.onCreate()

        AndroidLogcatLogger.installOnDebuggableApp(this, minPriority = VERBOSE)

        initKoin(
            appModule = module {
                single<Context> { this@AlkaaApp }
            },
        )
    }
}
