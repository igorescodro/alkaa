package com.escodro.home.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.escodro.home.presentation.Home

/**
 * Alkaa's Home Screen.
 */
class HomeScreen : Screen {

    @Composable
    override fun Content() {
        Home(
            onAboutClick = {},
            onTrackerClick = {},
            onOpenSourceClick = {},
            onCategorySheetOpen = {},
        )
    }
}
