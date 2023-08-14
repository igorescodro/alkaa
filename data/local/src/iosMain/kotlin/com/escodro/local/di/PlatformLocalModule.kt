package com.escodro.local.di

import com.escodro.local.provider.DriverFactory
import com.escodro.local.provider.IosDriverFactory
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind

internal actual val platformLocalModule = org.koin.dsl.module {
    singleOf(::IosDriverFactory) bind DriverFactory::class
}
