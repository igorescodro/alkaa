package com.escodro.appstate

import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import com.escodro.parcelable.CommonParcelable
import com.escodro.parcelable.CommonParcelize

/**
 * Alkaa App state.
 *
 * @property isCompactMode flag to indicate if the app is in compact mode
 */
@Stable
@CommonParcelize
data class AlkaaAppState(private val isCompactMode: Boolean) : AppState {

    /**
     * Verifies if the bottom bar should be shown.
     */
    override val shouldShowBottomBar: Boolean
        get() = isCompactMode

    /**
     * Verifies if the navigation rail should be shown.
     */
    override val shouldShowNavRail: Boolean
        get() = !shouldShowBottomBar
}

interface AppState : CommonParcelable {

    /**
     * Verifies if the bottom bar should be shown.
     */
    val shouldShowBottomBar: Boolean

    /**
     * Verifies if the navigation rail should be shown.
     */
    val shouldShowNavRail: Boolean
}

/**
 * Function to remember a [AlkaaAppState].
 *
 * @param windowSizeClass the window size class from current device
 */
@Composable
fun rememberAlkaaAppState(windowSizeClass: WindowSizeClass): AlkaaAppState {
    val isCompactMode = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact ||
        windowSizeClass.heightSizeClass == WindowHeightSizeClass.Compact
    return remember(isCompactMode) {
        AlkaaAppState(isCompactMode)
    }
}
