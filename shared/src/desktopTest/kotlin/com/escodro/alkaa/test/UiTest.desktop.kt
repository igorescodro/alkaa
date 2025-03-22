package com.escodro.alkaa.test

import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Koin module to provide the platform dependencies.
 */
actual val platformModule: Module
    get() = module { }
