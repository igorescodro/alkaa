package com.escodro.home.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.alkaa.appstate.AlkaaAppState
import com.escodro.home.presentation.Home

/**
 * Alkaa's Home Screen.
 */
class HomeScreen(private val appState: AlkaaAppState) : Screen {

    @Composable
    override fun Content() {
        Home(appState = appState)
    }
}
