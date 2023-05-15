package com.escodro.task.presentation.fake

import com.escodro.core.coroutines.CoroutineDebouncer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.runTest

@Suppress("GlobalCoroutineUsage")
internal class CoroutinesDebouncerFake : CoroutineDebouncer {

    override fun invoke(delay: Long, coroutineScope: CoroutineScope, function: suspend () -> Unit) {
        runTest { function() }
    }
}
