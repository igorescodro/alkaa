package com.escodro.preference.di

import com.escodro.preference.provider.AndroidAppInfoProvider
import com.escodro.preference.provider.AppInfoProvider
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformPreferenceModule = module {
    factoryOf(::AndroidAppInfoProvider) bind AppInfoProvider::class
}
