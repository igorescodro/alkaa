package com.escodro.alkaa.fake

import com.escodro.core.coroutines.CoroutineDebouncer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Suppress("GlobalCoroutineUsage")
internal class CoroutinesDebouncerFake : CoroutineDebouncer {

    @OptIn(DelicateCoroutinesApi::class)
    override fun invoke(delay: Long, coroutineScope: CoroutineScope, function: suspend () -> Unit) {
        GlobalScope.launch { function() }
    }
}
