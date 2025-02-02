package com.escodro.search.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.escodro.navigationapi.controller.NavEventController
import com.escodro.navigationapi.destination.HomeDestination
import com.escodro.navigationapi.event.TaskEvent
import com.escodro.navigationapi.provider.NavGraph
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
