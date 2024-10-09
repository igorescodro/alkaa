package com.escodro.search.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.escodro.navigation.controller.NavEventController
import com.escodro.navigation.destination.HomeDestination
import com.escodro.navigation.event.TaskEvent
import com.escodro.navigation.provider.NavGraph
import com.escodro.search.presentation.SearchSection

internal class SearchNavGraph : NavGraph {

    override val navGraph: NavGraphBuilder.(NavEventController) -> Unit = { navEventController ->
        composable<HomeDestination.Search> {
            SearchSection(
                onItemClick = { navEventController.sendEvent(TaskEvent.OnTaskClick(id = it)) },
            )
        }
    }
}
