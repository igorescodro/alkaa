package com.escodro.preference.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.escodro.preference.presentation.OpenSource

/**
 * Open source screen.
 */
internal class OpenSourceScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        OpenSource(onUpPress = { navigator.pop() })
    }
}
