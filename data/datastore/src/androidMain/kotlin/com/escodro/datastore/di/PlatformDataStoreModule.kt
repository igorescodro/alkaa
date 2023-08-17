package com.escodro.datastore.di

import com.escodro.datastore.AndroidDataStore
import org.koin.dsl.module

/**
 * Koin module to provide the DataStore implementation.
 */
internal actual val platformDataStoreModule = module {
    single { AndroidDataStore(context = get()).getDataStore() }
}
