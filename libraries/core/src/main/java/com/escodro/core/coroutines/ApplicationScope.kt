package com.escodro.core.coroutines

import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.named

/**
 * Qualifier to represent the [kotlinx.coroutines.CoroutineScope] attached to the application
 * lifecycle. This scope should be used in operations that must not be cancelled when the user
 * changes screens.
 */
val ApplicationScope: Qualifier = named("AlkaaAppScope")
