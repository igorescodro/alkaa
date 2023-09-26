package com.escodro.alkaa

import android.app.Application
import android.content.Context
import com.escodro.alarm.di.alarmModule
import com.escodro.alkaa.di.appModule
import com.escodro.category.di.categoryModule
import com.escodro.coroutines.di.coroutinesModule
import com.escodro.datastore.di.dataStoreModule
import com.escodro.designsystem.di.designSystemModule
import com.escodro.domain.di.domainModule
import com.escodro.glance.di.glanceModule
import com.escodro.local.di.localModule
import com.escodro.preference.di.preferenceModule
import com.escodro.repository.di.repositoryModule
import com.escodro.search.di.searchModule
import com.escodro.shared.di.sharedModule
import com.escodro.task.di.taskModule
import com.google.android.play.core.splitcompat.SplitCompat
import logcat.AndroidLogcatLogger
import logcat.LogPriority.VERBOSE
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.module.Module

/**
 * Alkaa [Application] class.
 */
class AlkaaApp : Application() {

    override fun onCreate() {
        super.onCreate()

        AndroidLogcatLogger.installOnDebuggableApp(this, minPriority = VERBOSE)

        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@AlkaaApp)
            modules(getAllModules())
        }
    }

    internal fun getAllModules(): List<Module> = listOf(
        appModule,
        sharedModule,
        coroutinesModule,
        designSystemModule,
        taskModule,
        alarmModule,
        categoryModule,
        searchModule,
        glanceModule,
        preferenceModule,
        domainModule,
        repositoryModule,
        localModule,
        dataStoreModule,
    )

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        SplitCompat.install(this)
    }
}
