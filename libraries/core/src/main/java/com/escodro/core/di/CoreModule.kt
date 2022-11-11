package com.escodro.core.di

import com.escodro.core.coroutines.CoroutineDebouncer
import com.escodro.core.coroutines.CoroutineDebouncerImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * Core dependency injection module.
 */
val coreModule = module {

    factoryOf(::CoroutineDebouncerImpl) bind CoroutineDebouncer::class
}
