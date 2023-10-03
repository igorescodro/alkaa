package com.escodro.preference.di

import com.escodro.preference.provider.AppInfoProvider
import com.escodro.preference.provider.BrowserProvider
import com.escodro.preference.provider.IosAppInfoProvider
import com.escodro.preference.provider.IosBrowserProvider
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformPreferenceModule = module {
    factoryOf(::IosBrowserProvider) bind BrowserProvider::class
    factoryOf(::IosAppInfoProvider) bind AppInfoProvider::class
}
