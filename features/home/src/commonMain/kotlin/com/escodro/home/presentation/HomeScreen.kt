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
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.escodro.appstate.rememberAlkaaAppState
import com.escodro.category.presentation.list.CategoryListSection
import com.escodro.preference.presentation.PreferenceSection
import com.escodro.search.presentation.SearchSection
import com.escodro.task.presentation.list.TaskListSection
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

/**
 * Alkaa Home screen.
 */
@Composable
fun Home() {
    val (currentSection, setCurrentSection) = rememberSaveable { mutableStateOf(HomeSection.Tasks) }
    val navItems = remember { HomeSection.entries.toImmutableList() }

    AlkaaHomeScaffold(
        homeSection = currentSection,
        navItems = navItems,
        setCurrentSection = setCurrentSection,
    )
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
private fun AlkaaHomeScaffold(
    homeSection: HomeSection,
    navItems: ImmutableList<HomeSection>,
    setCurrentSection: (HomeSection) -> Unit,
) {
    val appState = rememberAlkaaAppState(windowSizeClass = calculateWindowSizeClass())
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
                    AlkaaContent(homeSection = homeSection, modifier = Modifier)
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
    currentSection: HomeSection,
    onSectionSelect: (HomeSection) -> Unit,
    items: ImmutableList<HomeSection>,
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

@Composable
private fun AlkaaContent(
    homeSection: HomeSection,
    modifier: Modifier = Modifier,
) {
    when (homeSection) {
        HomeSection.Tasks -> TaskListSection(modifier = modifier)
        HomeSection.Search -> SearchSection(modifier = modifier)
        HomeSection.Categories -> CategoryListSection(modifier = modifier)
        HomeSection.Settings -> PreferenceSection(modifier = modifier)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AlkaaTopBar(currentSection: HomeSection) {
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
    currentSection: HomeSection,
    onSectionSelect: (HomeSection) -> Unit,
    items: ImmutableList<HomeSection>,
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
