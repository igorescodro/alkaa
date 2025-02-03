package com.escodro.navigationapi.extension

import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import com.escodro.navigationapi.destination.TopLevelDestinations
import com.escodro.navigationapi.marker.TopLevel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapLatest

/**
 * Retrieves the current top level destination from the back stack.
 */
@OptIn(ExperimentalCoroutinesApi::class)
fun NavHostController.currentTopLevelFlow(): Flow<TopLevel> =
    currentBackStack
        .mapLatest { entries ->
            // Find the first top level destination in the back stack
            entries.reversed().firstNotNullOfOrNull { entry ->
                TopLevelDestinations.firstOrNull { entry.destination.hasRoute(it::class) }
            }
        }.filterNotNull()
