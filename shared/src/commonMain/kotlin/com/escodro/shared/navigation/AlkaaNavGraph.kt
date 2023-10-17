package com.escodro.shared.navigation

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import com.escodro.category.navigation.categoryScreenModule
import com.escodro.home.navigation.HomeScreen
import com.escodro.preference.navigation.preferenceScreenModule
import com.escodro.task.navigation.taskScreenModule
import kotlinx.coroutines.flow.map

/**
 * Navigation graph of the application.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AlkaaNavGraph(modifier: Modifier = Modifier) {
    ScreenRegistry {
        taskScreenModule()
        categoryScreenModule()
        preferenceScreenModule()
    }

    BottomSheetNavigator(modifier = modifier) {
        closeKeyboardOnBottomSheetDismiss()
        Navigator(HomeScreen())
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun closeKeyboardOnBottomSheetDismiss() {
    val keyboardController = LocalSoftwareKeyboardController.current
    val bottomSheetNavigator = LocalBottomSheetNavigator.current
    LaunchedEffect(Unit) {
        snapshotFlow { bottomSheetNavigator.isVisible }
            .map { isVisible -> !isVisible }
            .collect { keyboardController?.hide() }
    }
}
