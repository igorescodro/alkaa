package com.escodro.core.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Debounce execution of a suspend function based on the given delay.
 */
class CoroutineDebouncer {

    private var debounceJob: Job? = null

    /**
     * Debounce execution of a suspend function based on the given delay.
     *
     * @param delay the debounce delay
     * @param coroutineScope the scope to be executed
     * @param function the suspend function to be executed
     */
    operator fun invoke(
        delay: Long = 500,
        coroutineScope: CoroutineScope,
        function: suspend () -> Unit
    ) {
        debounceJob?.cancel()
        debounceJob = coroutineScope.launch {
            delay(delay)
            function()
        }
    }
}
