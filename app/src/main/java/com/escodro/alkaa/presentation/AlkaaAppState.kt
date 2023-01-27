package com.escodro.alkaa.presentation

import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember

/**
 * Function to remember a [AlkaaAppState].
 *
 * @param windowSizeClass the window size class from current device
 */
@Composable
fun rememberAlkaaAppState(windowSizeClass: WindowSizeClass): AlkaaAppState {
    return remember(windowSizeClass) {
        AlkaaAppState(windowSizeClass)
    }
}

/**
 * Alkaa App state.
 *
 * @property windowSizeClass the window size class from current device
 */
@Stable
data class AlkaaAppState(private val windowSizeClass: WindowSizeClass) {

    /**
     * Verifies if the bottom bar should be shown.
     */
    val shouldShowBottomBar: Boolean
        get() = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact ||
            windowSizeClass.heightSizeClass == WindowHeightSizeClass.Compact

    /**
     * Verifies if the navigation rail should be shown.
     */
    val shouldShowNavRail: Boolean
        get() = !shouldShowBottomBar
}
