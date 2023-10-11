package com.escodro.preference.provider

import androidx.compose.runtime.Composable

/**
 * Provides the Tracker feature information on each platform. Since this is an Android dynamic
 * feature, it be always available for iOS.
 */
internal interface TrackerProvider {

    /**
     * Returns the Tracker feature content.
     */
    @Composable
    fun Content(onUpPress: () -> Unit)
}
