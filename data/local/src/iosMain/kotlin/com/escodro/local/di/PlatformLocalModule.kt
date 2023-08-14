package com.escodro.local.di

import com.escodro.local.provider.DriverFactory
import com.escodro.local.provider.IosDriverFactory
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal actual val platformLocalModule = module {
    singleOf(::IosDriverFactory) bind DriverFactory::class
}
