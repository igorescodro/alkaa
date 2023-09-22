package com.escodro.task.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import com.escodro.task.presentation.add.AddTaskBottomSheet

/**
 * Add task bottom sheet.
 */
internal class AddTaskBottomSheet : Screen {

    @Composable
    override fun Content() {
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        AddTaskBottomSheet(
            onHideBottomSheet = { bottomSheetNavigator.hide() },
        )
    }
}
