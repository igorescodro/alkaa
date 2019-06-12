package com.escodro.alkaa

import android.app.Application
import com.escodro.alarm.di.alarmModule
import com.escodro.alkaa.di.alkaaModules
import com.escodro.category.di.categoryModule
import com.escodro.core.di.coreModule
import com.escodro.domain.di.domainModule
import com.escodro.local.di.localModule
import com.escodro.task.di.taskModule
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
            modules(alkaaModules + coreModule + taskModule + localModule + domainModule + categoryModule + alarmModule)
        }
    }
}
