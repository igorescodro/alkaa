package com.escodro.alkaa.fake

import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import com.escodro.appstate.AppState
import com.escodro.navigationapi.destination.TopAppBarVisibleDestinations
import com.escodro.navigationapi.destination.TopLevelDestinations
import com.escodro.navigationapi.marker.TopLevel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapLatest

internal class AppStateFake(override val navHostController: NavHostController) : AppState {
    override val shouldShowBottomBar: Boolean = true
    override val shouldShowNavRail: Boolean = false

    @OptIn(ExperimentalCoroutinesApi::class)
    override val currentTopDestination: Flow<TopLevel> =
        navHostController.currentBackStackEntryFlow
            .mapLatest { navBackStackEntry ->
                val currentDestination = navBackStackEntry.destination
                TopLevelDestinations.find { currentDestination.hasRoute(it::class) }
            }.filterNotNull()

    @OptIn(ExperimentalCoroutinesApi::class)
    override val shouldShowTopAppBar: Flow<Boolean> =
        navHostController.currentBackStackEntryFlow
            .mapLatest { navBackStackEntry ->
                val currentDestination = navBackStackEntry.destination
                TopAppBarVisibleDestinations.any { currentDestination.hasRoute(it::class) }
            }
}
