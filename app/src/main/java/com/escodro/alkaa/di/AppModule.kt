package com.escodro.alkaa.di

import com.escodro.core.extension.getAlarmManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Application module.
 */
val appModule = module {
    factory { androidContext().getAlarmManager() }
}
