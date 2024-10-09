package com.escodro.navigation.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.escodro.navigation.controller.NavEventController
import com.escodro.navigation.destination.Destination
import com.escodro.navigation.marker.TopLevel
import com.escodro.navigation.provider.NavGraphProvider
import org.koin.compose.koinInject

@Composable
fun Navigator(
    startDestination: Destination,
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController(),
) {
    NavigatorLoader(
        startDestination = startDestination,
        navHostController = navHostController,
        modifier = modifier,
    )
}

@Composable
private fun NavigatorLoader(
    startDestination: Destination,
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
    navEventController: NavEventController = koinInject(),
    navGraphProvider: NavGraphProvider = koinInject(),
) {
    LaunchedEffect(Unit) {
        navEventController.destinationState.collect { destination ->
            when (destination) {
                is Destination.Back -> {
                    navHostController.popBackStack()
                }

                is TopLevel -> {
                    navHostController.navigate(destination) {
                        popUpTo(navHostController.graph.findStartDestination().id)
                        launchSingleTop = true
                    }
                }

                else -> {
                    navHostController.navigate(destination)
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
