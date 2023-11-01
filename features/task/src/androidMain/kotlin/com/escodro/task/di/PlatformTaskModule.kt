package com.escodro.task.di

import com.escodro.task.provider.AndroidRelativeDateTimeProvider
import com.escodro.task.provider.RelativeDateTimeProvider
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformTaskModule = module {
    factoryOf(::AndroidRelativeDateTimeProvider) bind RelativeDateTimeProvider::class
}
