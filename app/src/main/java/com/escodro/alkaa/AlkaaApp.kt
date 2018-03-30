package com.escodro.alkaa

import android.app.Application
import com.escodro.alkaa.di.Injector

/**
 * Alkaa [Application] class.
 *
 * @author Igor Escodro on 1/10/18.
 */
class AlkaaApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initializeComponent()
    }

    /**
     * Initialize Dagger components.
     */
    private fun initializeComponent() {
        Injector.initializeApplicationComponent(this)
    }
}
