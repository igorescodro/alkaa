package com.escodro.category.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.toRoute
import com.escodro.category.presentation.bottomsheet.CategoryBottomSheet
import com.escodro.category.presentation.list.CategoryListSection
import com.escodro.navigationapi.controller.NavEventController
import com.escodro.navigationapi.destination.CategoryDestination
import com.escodro.navigationapi.destination.HomeDestination
import com.escodro.navigationapi.event.CategoryEvent
import com.escodro.navigationapi.event.Event
import com.escodro.navigationapi.provider.NavGraph

internal class CategoryNavGraph : NavGraph {

    override val navGraph: NavGraphBuilder.(NavEventController) -> Unit = { navEventController ->
        composable<HomeDestination.CategoryList> {
            CategoryListSection(
                onAddClick = {
                    navEventController.sendEvent(CategoryEvent.OnNewCategoryClick)
                },
                onItemClick = {
                    navEventController.sendEvent(CategoryEvent.OnCategoryClick(it))
                },
            )
        }

        dialog<CategoryDestination.CategoryBottomSheet> { navEntry ->
            val route: CategoryDestination.CategoryBottomSheet = navEntry.toRoute()
            CategoryBottomSheet(
                categoryId = route.categoryId ?: 0L,
                onHideBottomSheet = { navEventController.sendEvent(Event.OnBack) },
            )
        }
    }
}
