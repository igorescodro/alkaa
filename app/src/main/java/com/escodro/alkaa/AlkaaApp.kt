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
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.slf4j.impl.HandroidLoggerAdapter

/**
 * Alkaa [Application] class.
 */
class AlkaaApp : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            HandroidLoggerAdapter.DEBUG = BuildConfig.DEBUG
        }

        startKoin {
            androidLogger(Level.NONE)
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
