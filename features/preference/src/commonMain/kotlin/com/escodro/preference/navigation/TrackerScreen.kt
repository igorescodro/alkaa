package com.escodro.preference.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.escodro.preference.provider.TrackerProvider
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Tracker screen.
 */
internal class TrackerScreen : Screen, KoinComponent {

    private val trackerProvider: TrackerProvider by inject()

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        trackerProvider.Content(onUpPress = { navigator.pop() })
    }
}
