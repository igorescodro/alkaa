package com.escodro.task.presentation.fake

import com.escodro.core.coroutines.CoroutineDebouncer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest

@Suppress("GlobalCoroutineUsage")
internal class CoroutinesDebouncerFake(private val testDispatcher: TestCoroutineDispatcher) :
    CoroutineDebouncer {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun invoke(delay: Long, coroutineScope: CoroutineScope, function: suspend () -> Unit) {
        testDispatcher.runBlockingTest { function() }
    }
}
