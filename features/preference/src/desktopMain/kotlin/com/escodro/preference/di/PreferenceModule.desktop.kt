package com.escodro.preference.di

import com.escodro.preference.provider.AppInfoProvider
import com.escodro.preference.provider.DesktopAppInfoProvider
import com.escodro.preference.provider.DesktopTrackerProvider
import com.escodro.preference.provider.TrackerProvider
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * Provides the platform-specific dependencies.
 */
internal actual val platformPreferenceModule = module {
    factoryOf(::DesktopAppInfoProvider) bind AppInfoProvider::class
    factoryOf(::DesktopTrackerProvider) bind TrackerProvider::class
}
