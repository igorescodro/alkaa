package com.escodro.navigation.provider

import androidx.navigation.NavGraphBuilder
import com.escodro.navigationapi.controller.NavEventController
import com.escodro.navigationapi.provider.NavGraph

internal class NavGraphProvider(
    private val navEventController: NavEventController,
    private val navGraphs: List<NavGraph>,
) {

    val navigationGraph: NavGraphBuilder.() -> Unit = {
        navGraphs.forEach { graph ->
            graph.navGraph(this, navEventController)
        }
    }
}
