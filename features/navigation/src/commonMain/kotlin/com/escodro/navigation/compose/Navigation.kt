package com.escodro.navigation.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.escodro.navigation.provider.NavGraphProvider
import com.escodro.navigationapi.controller.NavEventController
import com.escodro.navigationapi.destination.Destination
import com.escodro.navigationapi.extension.currentTopLevelFlow
import com.escodro.navigationapi.marker.TopLevel
import com.escodro.permission.api.BindPermissionEffect
import com.escodro.permission.api.PermissionController
import kotlinx.coroutines.flow.first
import org.koin.compose.koinInject

@Composable
fun Navigation(
    startDestination: Destination,
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavigationLoader(
        startDestination = startDestination,
        navHostController = navHostController,
        modifier = modifier,
    )
}

@Composable
private fun NavigationLoader(
    startDestination: Destination,
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
    navEventController: NavEventController = koinInject(),
    navGraphProvider: NavGraphProvider = koinInject(),
    permissionController: PermissionController = koinInject(),
) {
    // Required by Moko Permissions to bind in the lifecycle
    BindPermissionEffect(permissionController)

    LaunchedEffect(Unit) {
        navEventController.destinationState.collect { destination ->
            when (destination) {
                is Destination.Back -> {
                    navHostController.popBackStack()
                }

                is TopLevel -> {
                    // If is the same top level, do not navigate to avoid blinking
                    if (navHostController.currentBackStackEntry?.destination?.hasRoute(destination::class) == true) {
                        return@collect
                    }

                    // If the destination is the current top level, the state should not be saved
                    // This behavior will allow a "clean state" when clicking on the active item
                    val currentTopLevel: TopLevel = navHostController.currentTopLevelFlow().first()
                    val showSaveState = currentTopLevel != destination

                    navHostController.navigate(destination) {
                        popUpTo(navHostController.graph.findStartDestination().route ?: "") {
                            saveState = showSaveState
                        }
                        launchSingleTop = true
                        restoreState = showSaveState
                    }
                }

                else -> {
                    navHostController.navigate(destination) {
                        launchSingleTop = true
                    }
                }
            }
        }
    }

    NavHost(
        navController = navHostController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        navGraphProvider.navigationGraph(this)
    }
}
