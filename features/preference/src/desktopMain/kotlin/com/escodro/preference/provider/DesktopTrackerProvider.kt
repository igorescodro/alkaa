package com.escodro.preference.provider

import androidx.compose.runtime.Composable
import com.escodro.tracker.presentation.TrackerScreen

internal class DesktopTrackerProvider : TrackerProvider {

    @Composable
    override fun Content(onUpPress: () -> Unit) {
        TrackerScreen(onUpPress = onUpPress)
    }
}
