package com.escodro.alkaa.test

import androidx.compose.runtime.Composable
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Base class to be used in the tests.
 */
actual abstract class AlkaaBaseTest actual constructor()

/**
 * Koin module to provide the platform dependencies.
 */
actual val platformModule: Module
    get() = module { }
