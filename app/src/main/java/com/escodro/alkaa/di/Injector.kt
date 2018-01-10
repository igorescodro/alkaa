package com.escodro.alkaa.di

import com.escodro.alkaa.AlkaaApp
import com.escodro.alkaa.di.component.ApplicationComponent
import com.escodro.alkaa.di.component.DaggerApplicationComponent
import com.escodro.alkaa.di.module.AndroidModule

/**
 * Class responsible to initialize the [ApplicationComponent], setting all the applicable
 * [dagger.Module]s.
 *
 * @author Igor Escodro on 1/10/18.
 */
class Injector {

    companion object {
        @JvmStatic lateinit var applicationComponent: ApplicationComponent

        /**
         * Initialize the [ApplicationComponent], setting all the related [dagger.Module].
         *
         * @param application application class
         */
        fun initializeApplicationComponent(application: AlkaaApp) {
            applicationComponent = DaggerApplicationComponent.builder()
                    .androidModule(AndroidModule(application))
                    .build()
        }
    }
}
