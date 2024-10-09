package com.escodro.home.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.escodro.appstate.AppState
import com.escodro.navigation.compose.Navigation
import com.escodro.navigation.controller.NavEventController
import com.escodro.navigation.destination.HomeDestination
import com.escodro.navigation.destination.topLevelDestinations
import com.escodro.navigation.event.HomeEvent
import com.escodro.navigation.marker.TopLevel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

/**
 * Alkaa Home screen.
 */
@Composable
fun Home(appState: AppState) {
    HomeLoader(appState = appState)
}

@Composable
private fun HomeLoader(
    appState: AppState,
    navEventController: NavEventController = koinInject(),
) {
    var currentSection by rememberSaveable<MutableState<TopLevel>> {
        mutableStateOf(HomeDestination.TaskList)
    }
    val setCurrentSection = { section: TopLevel ->
        navEventController.sendEvent(HomeEvent.OnTabClick(section))
        currentSection = section
    }

    val navItems by rememberSaveable { mutableStateOf(topLevelDestinations) }

    AlkaaHomeScaffold(
        appState = appState,
        homeSection = currentSection,
        navItems = navItems.toImmutableList(),
        setCurrentSection = setCurrentSection,
    )
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
private fun AlkaaHomeScaffold(
    appState: AppState,
    homeSection: TopLevel,
    navItems: ImmutableList<TopLevel>,
    setCurrentSection: (TopLevel) -> Unit,
) {
    Scaffold(
        topBar = { AlkaaTopBar(currentSection = homeSection) },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        content = { paddingValues ->
            Row(
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .consumeWindowInsets(paddingValues)
                    .windowInsetsPadding(
                        WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal),
                    ),
            ) {
                if (appState.shouldShowNavRail) {
                    AlkaaNavRail(
                        currentSection = homeSection,
                        onSectionSelect = setCurrentSection,
                        items = navItems,
                        modifier = Modifier.consumeWindowInsets(paddingValues),
                    )
                }
                Column(Modifier.fillMaxSize()) {
                    Navigation(startDestination = HomeDestination.TaskList)
                }
            }
        },
        bottomBar = {
            if (appState.shouldShowBottomBar) {
                AlkaaBottomNav(
                    currentSection = homeSection,
                    onSectionSelect = setCurrentSection,
                    items = navItems,
                )
            }
        },
    )
}

@Composable
private fun AlkaaNavRail(
    currentSection: TopLevel,
    onSectionSelect: (TopLevel) -> Unit,
    items: ImmutableList<TopLevel>,
    modifier: Modifier = Modifier,
) {
    NavigationRail(modifier = modifier) {
        items.forEach { section ->
            val selected = section == currentSection
            NavigationRailItem(
                selected = selected,
                onClick = { onSectionSelect(section) },
                alwaysShowLabel = true,
                icon = { Icon(imageVector = section.icon, contentDescription = null) },
                label = { Text(stringResource(section.title)) },
                colors = NavigationRailItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                ),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AlkaaTopBar(currentSection: TopLevel) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Light),
                text = stringResource(currentSection.title),
                color = MaterialTheme.colorScheme.tertiary,
            )
        },
    )
}

@Composable
private fun AlkaaBottomNav(
    currentSection: TopLevel,
    onSectionSelect: (TopLevel) -> Unit,
    items: ImmutableList<TopLevel>,
) {
    BottomAppBar(containerColor = MaterialTheme.colorScheme.background) {
        items.forEach { section ->
            val selected = section == currentSection
            val title = section.title
            NavigationBarItem(
                selected = selected,
                onClick = { onSectionSelect(section) },
                icon = {
                    Icon(
                        imageVector = section.icon,
                        contentDescription = stringResource(title),
                    )
                },
                label = { Text(stringResource(title)) },
            )
        }
    }
}
