package com.escodro.coroutines.di

import com.escodro.coroutines.AppCoroutineScope
import com.escodro.coroutines.CoroutineDebouncer
import com.escodro.coroutines.CoroutineDebouncerImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * Coroutines library dependency injection module.
 */
val coroutinesModule = module {

    single { AppCoroutineScope() }
    factoryOf(::CoroutineDebouncerImpl) bind CoroutineDebouncer::class
}
