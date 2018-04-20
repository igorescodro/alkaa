package com.escodro.alkaa

import android.app.Application
import com.escodro.alkaa.di.alkaaModules
import org.koin.android.ext.android.startKoin

/**
 * Alkaa [Application] class.
 *
 * @author Igor Escodro on 1/10/18.
 */
class AlkaaApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin(this, alkaaModules)
    }
}
