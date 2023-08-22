package com.escodro.datastore.di

import com.escodro.datastore.IosDataStore
import org.koin.dsl.module

internal actual val platformDataStoreModule = module {
    single { IosDataStore().getDataStore() }
}
