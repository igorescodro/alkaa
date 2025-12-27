package com.escodro.appstate

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import com.escodro.navigationapi.controller.NavBackStack
import com.escodro.navigationapi.destination.Destination
import com.escodro.navigationapi.destination.HomeDestination

/**
 * Alkaa App state.
 */
@Stable
data class AlkaaAppState(val navBackStack: NavBackStack<Destination>)

/**
 * Function to remember a [AlkaaAppState].
 *
 * @param navBackStack the navigation back stack
 */
@Composable
fun rememberAlkaaAppState(
    navBackStack: NavBackStack<Destination> = remember {
        NavBackStack(HomeDestination.TaskList)
    },
): AlkaaAppState = remember(navBackStack) { AlkaaAppState(navBackStack) }
