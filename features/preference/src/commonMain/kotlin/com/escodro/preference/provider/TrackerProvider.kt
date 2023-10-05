package com.escodro.preference.provider

import androidx.compose.runtime.Composable

/**
 * Provides the Tracker feature information on each platform. Since this is a dynamic feature, it
 * will not be available at this moment on iOS.
 */
internal interface TrackerProvider {

    /**
     * Returns if the Tracker feature is enabled.
     */
    val isEnabled: Boolean

    /**
     * Returns the Tracker feature content.
     */
    @Composable
    fun Content(onUpPress: () -> Unit)
}
