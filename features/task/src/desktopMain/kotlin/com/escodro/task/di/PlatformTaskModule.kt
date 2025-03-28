package com.escodro.task.di

import com.escodro.task.provider.DesktopRelativeDateTimeProvider
import com.escodro.task.provider.RelativeDateTimeProvider
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * Provides the platform-specific dependencies.
 */
actual val platformTaskModule = module {
    factoryOf(::DesktopRelativeDateTimeProvider) bind RelativeDateTimeProvider::class
}
