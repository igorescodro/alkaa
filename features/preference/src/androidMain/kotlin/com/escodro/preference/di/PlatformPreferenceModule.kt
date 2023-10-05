package com.escodro.preference.di

import com.escodro.preference.provider.AndroidAppInfoProvider
import com.escodro.preference.provider.AndroidBrowserProvider
import com.escodro.preference.provider.AndroidTrackerProvider
import com.escodro.preference.provider.AppInfoProvider
import com.escodro.preference.provider.BrowserProvider
import com.escodro.preference.provider.TrackerProvider
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformPreferenceModule = module {
    factoryOf(::AndroidBrowserProvider) bind BrowserProvider::class
    factoryOf(::AndroidAppInfoProvider) bind AppInfoProvider::class
    factoryOf(::AndroidTrackerProvider) bind TrackerProvider::class
}
