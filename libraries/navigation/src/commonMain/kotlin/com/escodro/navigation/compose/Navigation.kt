package com.escodro.navigation.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.escodro.navigation.controller.NavEventController
import com.escodro.navigation.provider.NavGraphProvider
import com.escodro.navigationapi.destination.Destination
import com.escodro.navigationapi.marker.TopLevel
import com.escodro.permission.PermissionsControllerWrapper
import dev.icerock.moko.permissions.compose.BindEffect
import org.koin.compose.koinInject

@Composable
fun Navigation(
    startDestination: Destination,
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController(),
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
    permissionsControllerWrapper: PermissionsControllerWrapper = koinInject(),
) {
    // Required by Moko Permissions to bind in the lifecycle
    BindEffect(permissionsControllerWrapper.getInstance())

    LaunchedEffect(Unit) {
        navEventController.destinationState.collect { destination ->
            when (destination) {
                is Destination.Back -> {
                    navHostController.popBackStack()
                }

                is TopLevel -> {
                    navHostController.navigate(destination) {
                        popUpTo(navHostController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
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
