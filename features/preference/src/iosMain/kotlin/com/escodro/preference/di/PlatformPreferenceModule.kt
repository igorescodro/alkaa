package com.escodro.preference.di

import com.escodro.preference.provider.AppInfoProvider
import com.escodro.preference.provider.IosAppInfoProvider
import com.escodro.preference.provider.IosTrackerProvider
import com.escodro.preference.provider.TrackerProvider
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformPreferenceModule = module {
    factoryOf(::IosAppInfoProvider) bind AppInfoProvider::class
    factoryOf(::IosTrackerProvider) bind TrackerProvider::class
}
