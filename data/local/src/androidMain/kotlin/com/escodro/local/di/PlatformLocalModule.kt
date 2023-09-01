package com.escodro.local.di

import com.escodro.local.provider.AndroidDriverFactory
import com.escodro.local.provider.DriverFactory
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal actual val platformLocalModule = module {
    singleOf(::AndroidDriverFactory) bind DriverFactory::class
}
