package com.escodro.home.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.escodro.appstate.AppState
import com.escodro.designsystem.animation.TopBarEnterTransition
import com.escodro.designsystem.animation.TopBarExitTransition
import com.escodro.designsystem.components.topbar.MainTopBar
import com.escodro.navigation.compose.Navigation
import com.escodro.navigationapi.controller.NavEventController
import com.escodro.navigationapi.destination.HomeDestination
import com.escodro.navigationapi.destination.TopLevelDestinations
import com.escodro.navigationapi.event.HomeEvent
import com.escodro.navigationapi.marker.TopLevel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

/**
 * Alkaa Home screen.
 */
@Composable
fun Home(
    appState: AppState,
    modifier: Modifier = Modifier,
) {
    HomeLoader(
        appState = appState,
        modifier = modifier,
    )
}

@Composable
private fun HomeLoader(
    appState: AppState,
    modifier: Modifier = Modifier,
    navEventController: NavEventController = koinInject(),
) {
    val currentSection by appState.currentTopDestination
        .collectAsStateWithLifecycle(HomeDestination.TaskList)
    val navItems by rememberSaveable { mutableStateOf(TopLevelDestinations) }
    val setCurrentSection = { section: TopLevel ->
        navEventController.sendEvent(HomeEvent.OnTabClick(section))
    }

    AlkaaHomeScaffold(
        appState = appState,
        navItems = navItems.toImmutableList(),
        currentSection = currentSection,
        setCurrentSection = setCurrentSection,
        modifier = modifier,
    )
}

@Composable
private fun AlkaaHomeScaffold(
    appState: AppState,
    navItems: ImmutableList<TopLevel>,
    currentSection: TopLevel,
    setCurrentSection: (TopLevel) -> Unit,
    modifier: Modifier = Modifier,
) {
    val showTopBar by appState.shouldShowTopAppBar.collectAsStateWithLifecycle(true)
    val topBarOffset: Dp by animateDpAsState(
        targetValue = if (showTopBar) 0.dp else 64.dp,
        animationSpec = tween(easing = LinearEasing),
    )
    NavigationSuiteScaffold(
        navigationSuiteItems = {
            alkaaBottomNav(
                items = navItems,
                currentSection = currentSection,
                setCurrentSection = setCurrentSection,
            )
        },
        modifier = modifier,
    ) {
        Scaffold(
            topBar = {
                AnimatedVisibility(
                    visible = showTopBar,
                    enter = TopBarEnterTransition,
                    exit = TopBarExitTransition,
                ) {
                    MainTopBar(title = stringResource(currentSection.title))
                }
            },
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            content = { paddingValues ->
                val topPadding = paddingValues.calculateTopPadding() - topBarOffset
                Navigation(
                    startDestination = HomeDestination.TaskList,
                    navHostController = appState.navHostController,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            start = paddingValues.calculateStartPadding(LocalLayoutDirection.current),
                            top = if (topPadding > 0.dp) topPadding else 0.dp,
                            end = paddingValues.calculateEndPadding(LocalLayoutDirection.current),
                            bottom = paddingValues.calculateBottomPadding(),
                        ).consumeWindowInsets(paddingValues)
                        .windowInsetsPadding(
                            WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal),
                        ),
                )
            },
        )
    }
}

private fun NavigationSuiteScope.alkaaBottomNav(
    items: ImmutableList<TopLevel>,
    currentSection: TopLevel,
    setCurrentSection: (TopLevel) -> Unit,
) {
    items.forEach { section ->
        val selected = section == currentSection
        val title = section.title
        item(
            selected = selected,
            onClick = { setCurrentSection(section) },
            icon = {
                Icon(
                    imageVector = section.icon,
                    contentDescription = stringResource(title),
                )
            },
            label = { Text(stringResource(title)) },
            modifier = Modifier.testTag(title.toString()),
        )
    }
}
