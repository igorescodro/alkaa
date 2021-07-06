package com.escodro.alkaa.fake

import com.escodro.core.coroutines.CoroutineDebouncer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Suppress("GlobalCoroutineUsage")
internal class CoroutinesDebouncerFake : CoroutineDebouncer {

    override fun invoke(delay: Long, coroutineScope: CoroutineScope, function: suspend () -> Unit) {
        GlobalScope.launch { function() }
    }
}
