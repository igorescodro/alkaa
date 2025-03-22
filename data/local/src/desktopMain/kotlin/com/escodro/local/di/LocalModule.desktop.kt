package com.escodro.local.di

import com.escodro.local.provider.DesktopDriverFactory
import com.escodro.local.provider.DriverFactory
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * Provides the platform-specific dependencies.
 */
internal actual val platformLocalModule = module {
    singleOf(::DesktopDriverFactory) bind DriverFactory::class
}
