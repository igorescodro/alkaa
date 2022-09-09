package com.escodro.core.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

internal class CoroutineDebouncerImpl : CoroutineDebouncer {

    private var debounceJob: Job? = null

    override operator fun invoke(
        delay: Long,
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
