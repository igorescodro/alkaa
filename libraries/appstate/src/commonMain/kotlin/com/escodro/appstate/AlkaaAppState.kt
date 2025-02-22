package com.escodro.appstate

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
    override val navHostController: NavHostController,
) : AppState {

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
 * @param navHostController the [NavHostController] used to navigate between destinations
 */
@Composable
fun rememberAlkaaAppState(
    navHostController: NavHostController = rememberNavController(),
): AlkaaAppState = remember(navHostController) {
    AlkaaAppState(navHostController)
}
