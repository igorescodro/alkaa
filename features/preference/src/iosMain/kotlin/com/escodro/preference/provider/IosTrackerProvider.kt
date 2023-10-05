package com.escodro.preference.provider

import androidx.compose.runtime.Composable

internal class IosTrackerProvider : TrackerProvider {

    override val isEnabled: Boolean = false

    @Composable
    override fun Content(onUpPress: () -> Unit) {
        throw IllegalStateException("Tracker is not available on iOS")
    }
}
