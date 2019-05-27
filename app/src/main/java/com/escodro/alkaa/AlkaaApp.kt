package com.escodro.alkaa

import android.app.Application
import com.escodro.alkaa.di.alkaaModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

/**
 * Alkaa [Application] class.
 */
class AlkaaApp : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            printLogger()
            androidContext(this@AlkaaApp)
            modules(alkaaModules)
        }
    }
}
