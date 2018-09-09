package com.escodro.alkaa

import android.app.Application
import com.escodro.alkaa.di.alkaaModules
import org.koin.android.ext.android.startKoin
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

        startKoin(this, alkaaModules)
    }
}
