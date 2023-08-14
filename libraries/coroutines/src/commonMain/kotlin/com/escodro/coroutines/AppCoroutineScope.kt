package com.escodro.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Custom [CoroutineScope] wrapper representing the application life state. This is required to make
 * sure that the scope is never cancelled.
 *
 * @property context the coroutine context, by default it's [SupervisorJob] and [Dispatchers.Main].
 */
class AppCoroutineScope(val context: CoroutineContext = SupervisorJob() + Dispatchers.Main) {

    private val coroutineScope = object : CoroutineScope {
        override val coroutineContext: CoroutineContext
            get() = context
    }

    /**
     * Launches a new coroutine without blocking the current thread and returns a reference to the
     * coroutine as a Job. The coroutine is cancelled when the resulting job is cancelled.
     *
     * This function is a pass-through to the [CoroutineScope.launch] one.
     *
     * For more information, take a look at the [CoroutineScope.launch] documentation.
     *
     * @param context additional to [CoroutineScope.coroutineContext] context of the coroutine
     * @param start coroutine start option. The default value is [CoroutineStart.DEFAULT]
     * @param block the coroutine code which will be invoked in the context of the provided scope
     */
    fun launch(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit,
    ): Job = coroutineScope.launch(context = context, start = start, block = block)
}
