package com.escodro.shared.navigation

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import com.escodro.category.navigation.categoryScreenModule
import com.escodro.home.navigation.HomeScreen
import com.escodro.preference.navigation.preferenceScreenModule
import com.escodro.task.navigation.taskScreenModule

/**
 * Navigation graph of the application.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AlkaaNavGraph() {
    ScreenRegistry {
        taskScreenModule()
        categoryScreenModule()
        preferenceScreenModule()
    }

    BottomSheetNavigator {
        Navigator(HomeScreen())
    }
}
