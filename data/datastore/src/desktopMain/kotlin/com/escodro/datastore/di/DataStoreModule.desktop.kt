package com.escodro.datastore.di

import com.escodro.datastore.DesktopDataStore
import org.koin.dsl.module

/**
 * Provides the platform-specific dependencies.
 */
internal actual val platformDataStoreModule = module {
    single { DesktopDataStore().getDataStore() }
}
