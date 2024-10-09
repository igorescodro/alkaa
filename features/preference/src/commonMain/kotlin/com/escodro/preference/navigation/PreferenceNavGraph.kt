package com.escodro.preference.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.escodro.navigation.controller.NavEventController
import com.escodro.navigation.destination.HomeDestination
import com.escodro.navigation.destination.PreferenceDestination
import com.escodro.navigation.event.Event
import com.escodro.navigation.event.PreferenceEvent
import com.escodro.navigation.provider.NavGraph
import com.escodro.preference.presentation.AboutScreen
import com.escodro.preference.presentation.OpenSource
import com.escodro.preference.presentation.PreferenceSection
import com.escodro.preference.provider.TrackerProvider

internal class PreferenceNavGraph(
    private val trackerProvider: TrackerProvider,
) : NavGraph {
    override val navGraph: NavGraphBuilder.(NavEventController) -> Unit = { navEventController ->
        composable<HomeDestination.Preferences> {
            PreferenceSection(
                onAboutClick = { navEventController.sendEvent(PreferenceEvent.OnAboutClick) },
                onOpenSourceClick = { navEventController.sendEvent(PreferenceEvent.OnLicensesClick) },
                onTrackerClick = { navEventController.sendEvent(PreferenceEvent.OnTrackerClick) },
            )
        }

        composable<PreferenceDestination.About> {
            AboutScreen(
                onUpPress = { navEventController.sendEvent(Event.OnBack) },
            )
        }

        composable<PreferenceDestination.Licenses> {
            OpenSource(
                onUpPress = { navEventController.sendEvent(Event.OnBack) },
            )
        }

        composable<PreferenceDestination.Tracker> {
            trackerProvider.Content(
                onUpPress = { navEventController.sendEvent(Event.OnBack) },
            )
        }
    }
}
