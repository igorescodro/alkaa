@file:Suppress("MatchingDeclarationName")

package com.escodro.alkaa.test

import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Base class to be used in the tests.
 */
@Suppress("AbstractClassCanBeInterface")
actual abstract class AlkaaTest actual constructor()

/**
 * Koin module to provide the platform dependencies.
 */
actual val platformModule: Module
    get() = module { }
