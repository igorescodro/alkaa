package com.escodro.search.navigation

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.ui.NavDisplay
import com.escodro.designsystem.animation.FadeInTransition
import com.escodro.designsystem.animation.FadeOutTransition
import com.escodro.navigationapi.controller.NavEventController
import com.escodro.navigationapi.destination.Destination
import com.escodro.navigationapi.destination.HomeDestination
import com.escodro.navigationapi.event.TaskEvent
import com.escodro.navigationapi.extension.isSinglePane
import com.escodro.navigationapi.provider.NavGraph
import com.escodro.search.presentation.SearchSection

internal class SearchNavGraph : NavGraph {

    override val navGraph:
        EntryProviderScope<Destination>.(NavEventController) -> Unit = { navEventController ->
            entry<HomeDestination.Search>(
                metadata = NavDisplay.transitionSpec { FadeInTransition } +
                    NavDisplay.popTransitionSpec { FadeOutTransition } +
                    NavDisplay.predictivePopTransitionSpec { FadeOutTransition },
            ) {
                SearchSection(
                    isSinglePane = currentWindowAdaptiveInfo().windowSizeClass.isSinglePane(),
                    onItemClick = { taskId ->
                        navEventController.sendEvent(TaskEvent.OnTaskClick(id = taskId))
                    },
                )
            }
        }
}
