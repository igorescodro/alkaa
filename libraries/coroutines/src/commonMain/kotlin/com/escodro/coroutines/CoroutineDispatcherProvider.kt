package com.escodro.coroutines

import kotlinx.coroutines.CoroutineDispatcher

/**
 * Interface to provide different [CoroutineDispatcher] implementations.
 */
interface CoroutineDispatcherProvider {

    /**
     * Dispatcher optimized for IO operations.
     */
    val io: CoroutineDispatcher

    /**
     * Dispatcher optimized for Main operations.
     */
    val main: CoroutineDispatcher

    /**
     * Default dispatcher.
     */
    val default: CoroutineDispatcher
}
