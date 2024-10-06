package com.escodro.shared.di

import com.escodro.alarm.di.alarmModule
import com.escodro.category.di.categoryModule
import com.escodro.coroutines.di.coroutinesModule
import com.escodro.datastore.di.dataStoreModule
import com.escodro.designsystem.di.designSystemModule
import com.escodro.domain.di.domainModule
import com.escodro.local.di.localModule
import com.escodro.navigation.di.navigationModule
import com.escodro.preference.di.preferenceModule
import com.escodro.repository.di.repositoryModule
import com.escodro.search.di.searchModule
import com.escodro.task.di.taskModule
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Initializes the Koin modules.
 */
fun initKoin() = initKoin(module { })

/**
 * Initializes the Koin modules.
 *
 * @param appModule the app module to be included
 */
fun initKoin(appModule: Module = module { }) {
    startKoin {
        modules(appModules + appModule)
    }
}

internal val appModules = listOf(
    sharedModule,
    taskModule,
    alarmModule,
    categoryModule,
    searchModule,
    preferenceModule,
    domainModule,
    repositoryModule,
    localModule,
    dataStoreModule,
    coroutinesModule,
    designSystemModule,
    navigationModule,
)
