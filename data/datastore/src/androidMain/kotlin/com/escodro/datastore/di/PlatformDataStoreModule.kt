package com.escodro.datastore.di

import com.escodro.datastore.AndroidDataStore
import org.koin.dsl.module

internal actual val platformDataStoreModule = module {
    single { AndroidDataStore(context = get()).getDataStore() }
}
