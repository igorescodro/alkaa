package com.escodro.category.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.ui.NavDisplay
import com.escodro.category.presentation.bottomsheet.CategoryBottomSheet
import com.escodro.category.presentation.list.CategoryListSection
import com.escodro.designsystem.animation.FadeInTransition
import com.escodro.designsystem.animation.FadeOutTransition
import com.escodro.navigationapi.controller.NavEventController
import com.escodro.navigationapi.destination.CategoryDestination
import com.escodro.navigationapi.destination.Destination
import com.escodro.navigationapi.destination.HomeDestination
import com.escodro.navigationapi.event.CategoryEvent
import com.escodro.navigationapi.event.Event
import com.escodro.navigationapi.provider.NavGraph

internal class CategoryNavGraph : NavGraph {

    override val navGraph:
        EntryProviderScope<Destination>.(NavEventController) -> Unit = { navEventController ->
            entry<HomeDestination.CategoryList>(
                metadata = NavDisplay.transitionSpec { FadeInTransition } +
                    NavDisplay.popTransitionSpec { FadeOutTransition } +
                    NavDisplay.predictivePopTransitionSpec { FadeOutTransition },
            ) {
                CategoryListSection(
                    onAddClick = {
                        navEventController.sendEvent(CategoryEvent.OnNewCategoryClick)
                    },
                    onItemClick = { categoryId ->
                        navEventController.sendEvent(CategoryEvent.OnCategoryClick(categoryId))
                    },
                )
            }

            entry<CategoryDestination.CategoryBottomSheet>(
                metadata = DialogSceneStrategy.dialog(),
            ) { entry ->
                CategoryBottomSheet(
                    categoryId = entry.categoryId ?: 0L,
                    onHideBottomSheet = { navEventController.sendEvent(Event.OnBack) },
                )
            }
        }
}
