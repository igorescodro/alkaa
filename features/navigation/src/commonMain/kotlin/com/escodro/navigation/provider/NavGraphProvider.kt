package com.escodro.navigation.provider

import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.entryProvider
import com.escodro.navigationapi.controller.NavEventController
import com.escodro.navigationapi.destination.Destination
import com.escodro.navigationapi.provider.NavGraph

internal class NavGraphProvider(
    private val navEventController: NavEventController,
    private val navGraphs: List<NavGraph>,
) {
    val navigationGraph: (Destination) -> NavEntry<Destination> = entryProvider {
        navGraphs.forEach { navGraph ->
            navGraph.navGraph(this, navEventController)
        }
    }
}
