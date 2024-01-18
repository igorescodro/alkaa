package com.escodro.category.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import com.escodro.category.presentation.bottomsheet.CategoryBottomSheet

/**
 * Category Bottom Sheet.
 *
 * @param categoryId the category id to be edited, null if it is a new category
 */
internal data class CategoryBottomSheet(val categoryId: Long?) : Screen {

    @Composable
    override fun Content() {
        val bottomSheetNavigator = LocalBottomSheetNavigator.current

        CategoryBottomSheet(
            categoryId = categoryId ?: 0L,
            onHideBottomSheet = { bottomSheetNavigator.hide() },
        )
    }
}
