package com.escodro.preference.navigation

import cafe.adriel.voyager.core.registry.screenModule
import com.escodro.navigation.AlkaaDestinations

/**
 * Preference screen navigation module.
 */
val preferenceScreenModule = screenModule {
    register<AlkaaDestinations.Preferences.About> {
        AboutScreen()
    }
    register<AlkaaDestinations.Preferences.OpenSource> {
        OpenSourceScreen()
    }
    register<AlkaaDestinations.Preferences.Tracker> {
        TrackerScreen()
    }
}
