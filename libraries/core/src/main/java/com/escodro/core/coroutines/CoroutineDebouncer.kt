package com.escodro.core.coroutines

import kotlinx.coroutines.CoroutineScope

/**
 * Debounce execution of a suspend function based on the given delay.
 */
interface CoroutineDebouncer {

    /**
     * Debounce execution of a suspend function based on the given delay.
     *
     * @param delay the debounce delay
     * @param coroutineScope the scope to be executed
     * @param function the suspend function to be executed
     */
    operator fun invoke(
        delay: Long = 300,
        coroutineScope: CoroutineScope,
        function: suspend () -> Unit
    )
}
