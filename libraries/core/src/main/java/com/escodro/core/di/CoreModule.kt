package com.escodro.core.di

import com.escodro.core.coroutines.CoroutineDebouncer
import com.escodro.core.coroutines.CoroutineDebouncerImpl
import org.koin.dsl.module

/**
 * Core dependency injection module.
 */
val coreModule = module {

    factory<CoroutineDebouncer> { CoroutineDebouncerImpl() }
}
