package com.escodro.alkaa

import android.app.Application
import com.escodro.alkaa.di.alkaaModules
import org.koin.android.ext.android.startKoin

/**
 * Alkaa [Application] class.
 */
class AlkaaApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin(this, alkaaModules)
    }
}
