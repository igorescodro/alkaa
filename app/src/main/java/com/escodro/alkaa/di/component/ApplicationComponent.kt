package com.escodro.alkaa.di.component

import com.escodro.alkaa.AlkaaApp
import com.escodro.alkaa.di.module.AndroidModule
import com.escodro.alkaa.ui.task.MainActivity
import dagger.Component
import javax.inject.Singleton

/**
 * Main application [Component].
 *
 * @author Igor Escodro on 1/10/18.
 */
@Singleton
@Component(modules = [(AndroidModule::class)])
interface ApplicationComponent {

    fun inject(mainActivity: AlkaaApp)

    fun inject(mainActivity: MainActivity)
}
