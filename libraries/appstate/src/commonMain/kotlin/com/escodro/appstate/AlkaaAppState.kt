package com.escodro.appstate

import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.escodro.navigationapi.destination.TopAppBarVisibleDestinations
import com.escodro.navigationapi.extension.currentTopLevelFlow
import com.escodro.navigationapi.marker.TopLevel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

/**
 * Alkaa App state.
 *
 * @property isCompactMode flag to indicate if the app is in compact mode
 */
@Stable
data class AlkaaAppState(
    private val isCompactMode: Boolean,
    override val navHostController: NavHostController,
) : AppState {

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

    override val currentTopDestination: Flow<TopLevel> =
        navHostController.currentTopLevelFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    override val shouldShowTopAppBar: Flow<Boolean> =
        navHostController.currentBackStackEntryFlow
            .mapLatest { navBackStackEntry ->
                val currentDestination = navBackStackEntry.destination
                TopAppBarVisibleDestinations.any { currentDestination.hasRoute(it::class) }
            }
}

interface AppState {

    /**
     * Verifies if the bottom bar should be shown.
     */
    val shouldShowBottomBar: Boolean

    /**
     * Verifies if the navigation rail should be shown.
     */
    val shouldShowNavRail: Boolean

    /**
     * The [NavHostController] used to navigate between destinations.
     */
    val navHostController: NavHostController

    /**
     * The current top level destination.
     */
    val currentTopDestination: Flow<TopLevel>

    /**
     * Verifies if the top app bar should be shown.
     */
    val shouldShowTopAppBar: Flow<Boolean>
}

/**
 * Function to remember a [AlkaaAppState].
 *
 * @param windowSizeClass the window size class from current device
 */
@Composable
fun rememberAlkaaAppState(
    windowSizeClass: WindowSizeClass,
    navHostController: NavHostController = rememberNavController(),
): AlkaaAppState {
    val isCompactMode = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact ||
        windowSizeClass.heightSizeClass == WindowHeightSizeClass.Compact
    return remember(isCompactMode, navHostController) {
        AlkaaAppState(isCompactMode, navHostController)
    }
}
