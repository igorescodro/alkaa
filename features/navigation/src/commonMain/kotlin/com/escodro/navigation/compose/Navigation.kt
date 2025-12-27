package com.escodro.navigation.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.ui.NavDisplay
import com.escodro.navigation.provider.NavGraphProvider
import com.escodro.navigationapi.controller.NavBackStack
import com.escodro.navigationapi.controller.NavEventController
import com.escodro.navigationapi.destination.Destination
import com.escodro.navigationapi.marker.TopLevel
import com.escodro.permission.api.BindPermissionEffect
import com.escodro.permission.api.PermissionController
import org.koin.compose.koinInject

@Composable
fun Navigation(
    navBackStack: NavBackStack<Destination>,
    modifier: Modifier = Modifier,
) {
    NavigationLoader(
        navBackStack = navBackStack,
        modifier = modifier,
    )
}

@Composable
private fun NavigationLoader(
    navBackStack: NavBackStack<Destination>,
    modifier: Modifier = Modifier,
    navEventController: NavEventController = koinInject(),
    navGraphProvider: NavGraphProvider = koinInject(),
    permissionController: PermissionController = koinInject(),
) {
    // Required by Moko Permissions to bind in the lifecycle
    BindPermissionEffect(permissionController)

    val dialogStrategy = remember { DialogSceneStrategy<Destination>() }

    LaunchedEffect(Unit) {
        navEventController.destinationState.collect { destination ->
            when (destination) {
                is Destination.Back -> {
                    navBackStack.removeLast()
                }

                is TopLevel -> {
                    if (destination == navBackStack.topLevelKey) {
                        navBackStack.clearTopLevel(destination)
                    } else {
                        navBackStack.addTopLevel(destination)
                    }
                }

                else -> {
                    navBackStack.add(destination)
                }
            }
        }
    }

    NavDisplay(
        backStack = navBackStack.backStack,
        onBack = { navBackStack.removeLast() },
        sceneStrategy = dialogStrategy,
        entryProvider = navGraphProvider.navigationGraph,
        modifier = modifier,
    )
}
