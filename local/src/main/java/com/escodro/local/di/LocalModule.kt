package com.escodro.local.di

import com.escodro.local.provider.DaoProvider
import com.escodro.local.provider.DatabaseProvider
import org.koin.dsl.module

val localModule = module {
    single { DatabaseProvider(get()) }
    single { DaoProvider(get()) }
}
