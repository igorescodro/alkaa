package com.escodro.alkaa.di.component

import com.escodro.alkaa.di.module.AndroidModule
import com.escodro.alkaa.di.module.DatabaseModule
import com.escodro.alkaa.ui.task.TaskActivity
import com.escodro.alkaa.ui.task.TaskContract
import dagger.Component
import javax.inject.Singleton

/**
 * Main application [Component].
 *
 * @author Igor Escodro on 1/10/18.
 */
@Singleton
@Component(modules = [AndroidModule::class, DatabaseModule::class])
interface ApplicationComponent {

    fun inject(taskActivity: TaskActivity)

    fun inject(taskContract: TaskContract)
}
