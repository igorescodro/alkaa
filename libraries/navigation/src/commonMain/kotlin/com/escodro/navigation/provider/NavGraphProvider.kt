package com.escodro.navigation.provider

import androidx.navigation.NavGraphBuilder
import com.escodro.navigation.controller.NavEventController

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
