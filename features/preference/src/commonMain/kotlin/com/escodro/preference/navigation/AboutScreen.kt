package com.escodro.preference.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.escodro.preference.presentation.AboutScreen

/**
 * About screen.
 */
internal class AboutScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        AboutScreen(
            onUpPress = { navigator.pop() },
        )
    }
}
