package com.escodro.alkaa

import android.app.Application
import android.content.Context
import com.escodro.alarm.di.alarmModule
import com.escodro.alkaa.di.appModule
import com.escodro.category.di.categoryModule
import com.escodro.core.di.coreModule
import com.escodro.domain.di.domainModule
import com.escodro.local.di.localModule
import com.escodro.repository.di.repositoryModule
import com.escodro.search.di.searchModule
import com.escodro.task.di.taskModule
import com.google.android.play.core.splitcompat.SplitCompat
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
            modules(
                appModule +
                    coreModule +
                    taskModule +
                    alarmModule +
                    categoryModule +
                    searchModule +
                    domainModule +
                    repositoryModule +
                    localModule
            )
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        SplitCompat.install(this)
    }
}
