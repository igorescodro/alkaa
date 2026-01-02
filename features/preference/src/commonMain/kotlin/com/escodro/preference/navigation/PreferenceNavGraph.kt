package com.escodro.preference.navigation

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.ui.NavDisplay
import com.escodro.designsystem.animation.FadeInTransition
import com.escodro.designsystem.animation.FadeOutTransition
import com.escodro.designsystem.animation.SlideInHorizontallyTransition
import com.escodro.designsystem.animation.SlideOutHorizontallyTransition
import com.escodro.navigationapi.controller.NavEventController
import com.escodro.navigationapi.destination.Destination
import com.escodro.navigationapi.destination.HomeDestination
import com.escodro.navigationapi.destination.PreferenceDestination
import com.escodro.navigationapi.event.Event
import com.escodro.navigationapi.event.PreferenceEvent
import com.escodro.navigationapi.extension.isSinglePane
import com.escodro.navigationapi.provider.NavGraph
import com.escodro.preference.presentation.AboutScreen
import com.escodro.preference.presentation.OpenSource
import com.escodro.preference.presentation.PreferenceSection
import com.escodro.tracker.presentation.TrackerScreen

internal class PreferenceNavGraph : NavGraph {
    override val navGraph:
        EntryProviderScope<Destination>.(NavEventController) -> Unit = { navEventController ->
            entry<HomeDestination.Preferences>(
                metadata = NavDisplay.transitionSpec { FadeInTransition } +
                    NavDisplay.popTransitionSpec { FadeOutTransition } +
                    NavDisplay.predictivePopTransitionSpec { FadeOutTransition },
            ) {
                PreferenceSection(
                    isSinglePane = currentWindowAdaptiveInfo().windowSizeClass.isSinglePane(),
                    onAboutClick = { navEventController.sendEvent(PreferenceEvent.OnAboutClick) },
                    onOpenSourceClick = { navEventController.sendEvent(PreferenceEvent.OnLicensesClick) },
                    onTrackerClick = { navEventController.sendEvent(PreferenceEvent.OnTrackerClick) },
                )
            }
            entry<PreferenceDestination.About>(
                metadata = NavDisplay.transitionSpec { SlideInHorizontallyTransition } +
                    NavDisplay.popTransitionSpec { SlideOutHorizontallyTransition } +
                    NavDisplay.predictivePopTransitionSpec { SlideOutHorizontallyTransition },
            ) {
                AboutScreen(
                    isSinglePane = true,
                    onUpPress = { navEventController.sendEvent(Event.OnBack) },
                )
            }

            entry<PreferenceDestination.Licenses>(
                metadata = NavDisplay.transitionSpec { SlideInHorizontallyTransition } +
                    NavDisplay.popTransitionSpec { SlideOutHorizontallyTransition } +
                    NavDisplay.predictivePopTransitionSpec { SlideOutHorizontallyTransition },
            ) {
                OpenSource(
                    isSinglePane = true,
                    onUpPress = { navEventController.sendEvent(Event.OnBack) },
                )
            }

            entry<PreferenceDestination.Tracker>(
                metadata = NavDisplay.transitionSpec { SlideInHorizontallyTransition } +
                    NavDisplay.popTransitionSpec { SlideOutHorizontallyTransition } +
                    NavDisplay.predictivePopTransitionSpec { SlideOutHorizontallyTransition },
            ) {
                TrackerScreen(
                    onUpPress = { navEventController.sendEvent(Event.OnBack) },
                )
            }
        }
}
