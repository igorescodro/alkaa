package com.escodro.alkaa.fake

import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import com.escodro.appstate.AppState
import com.escodro.navigationapi.destination.HomeDestination
import com.escodro.navigationapi.destination.topLevelDestinations
import com.escodro.navigationapi.marker.TopLevel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

internal class AppStateFake(override val navHostController: NavHostController) : AppState {
    override val shouldShowBottomBar: Boolean = true
    override val shouldShowNavRail: Boolean = false

    @OptIn(ExperimentalCoroutinesApi::class)
    override val currentTopDestination: Flow<TopLevel>
        get() = navHostController.currentBackStackEntryFlow
            .mapLatest { navBackStackEntry ->
                val currentDestination = navBackStackEntry.destination
                topLevelDestinations.find { currentDestination.hasRoute(it::class) }
                    ?: HomeDestination.TaskList
            }
}
