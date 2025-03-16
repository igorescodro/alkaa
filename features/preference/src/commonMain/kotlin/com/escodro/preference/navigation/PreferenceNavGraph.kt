package com.escodro.preference.navigation

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.escodro.designsystem.animation.SlideInHorizontallyTransition
import com.escodro.designsystem.animation.SlideOutHorizontallyTransition
import com.escodro.navigationapi.controller.NavEventController
import com.escodro.navigationapi.destination.HomeDestination
import com.escodro.navigationapi.destination.PreferenceDestination
import com.escodro.navigationapi.event.Event
import com.escodro.navigationapi.event.PreferenceEvent
import com.escodro.navigationapi.extension.isCompact
import com.escodro.navigationapi.provider.NavGraph
import com.escodro.preference.presentation.AboutScreen
import com.escodro.preference.presentation.OpenSource
import com.escodro.preference.presentation.PreferenceSection
import com.escodro.preference.provider.TrackerProvider

internal class PreferenceNavGraph(
    private val trackerProvider: TrackerProvider,
) : NavGraph {
    override val navGraph: NavGraphBuilder.(NavEventController) -> Unit = { navEventController ->
        composable<HomeDestination.Preferences> {
            val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
            PreferenceSection(
                isCompact = windowSizeClass.isCompact(),
                onAboutClick = { navEventController.sendEvent(PreferenceEvent.OnAboutClick) },
                onOpenSourceClick = { navEventController.sendEvent(PreferenceEvent.OnLicensesClick) },
                onTrackerClick = { navEventController.sendEvent(PreferenceEvent.OnTrackerClick) },
            )
        }

        composable<PreferenceDestination.About>(
            enterTransition = { SlideInHorizontallyTransition },
            exitTransition = { SlideOutHorizontallyTransition },
        ) {
            AboutScreen(
                isCompact = true,
                onUpPress = { navEventController.sendEvent(Event.OnBack) },
            )
        }

        composable<PreferenceDestination.Licenses>(
            enterTransition = { SlideInHorizontallyTransition },
            exitTransition = { SlideOutHorizontallyTransition },
        ) {
            OpenSource(
                isCompact = true,
                onUpPress = { navEventController.sendEvent(Event.OnBack) },
            )
        }

        composable<PreferenceDestination.Tracker>(
            enterTransition = { SlideInHorizontallyTransition },
            exitTransition = { SlideOutHorizontallyTransition },
        ) {
            trackerProvider.Content(
                onUpPress = { navEventController.sendEvent(Event.OnBack) },
            )
        }
    }
}
