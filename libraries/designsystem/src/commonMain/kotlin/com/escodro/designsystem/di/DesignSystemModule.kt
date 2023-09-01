package com.escodro.designsystem.di

import org.koin.core.module.Module
import org.koin.dsl.module

val designSystemModule = module {
    includes(platformDesignSystemModule)
}

/**
 * Provides the platform-specific dependencies.
 */
expect val platformDesignSystemModule: Module
