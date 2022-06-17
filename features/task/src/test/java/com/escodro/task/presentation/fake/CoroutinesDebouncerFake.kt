package com.escodro.task.presentation.fake

import com.escodro.core.coroutines.CoroutineDebouncer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest

@Suppress("GlobalCoroutineUsage")
internal class CoroutinesDebouncerFake : CoroutineDebouncer {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun invoke(delay: Long, coroutineScope: CoroutineScope, function: suspend () -> Unit) {
        runTest { function() }
    }
}
