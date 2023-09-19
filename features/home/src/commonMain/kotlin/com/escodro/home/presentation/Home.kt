package com.escodro.home.presentation

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.escodro.category.presentation.list.CategoryListSection
import com.escodro.task.presentation.list.TaskListSection
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

/**
 * Alkaa Home screen.
 */
@Suppress("LongParameterList")
@Composable
fun Home(
    onTaskClick: (Long) -> Unit,
    onAboutClick: () -> Unit,
    onTrackerClick: () -> Unit,
    onOpenSourceClick: () -> Unit,
    onTaskSheetOpen: () -> Unit,
    onCategorySheetOpen: (Long?) -> Unit,
) {
    val (currentSection, setCurrentSection) = rememberSaveable { mutableStateOf(HomeSection.Tasks) }
    val navItems = remember { HomeSection.values().toList().toImmutableList() }

    val actions = remember {
        object : HomeActions {
            override val onTaskClick = onTaskClick
            override val onAboutClick = onAboutClick
            override val onTrackerClick = onTrackerClick
            override val onOpenSourceClick = onOpenSourceClick
            override val onTaskSheetOpen = onTaskSheetOpen
            override val onCategorySheetOpen = onCategorySheetOpen
            override val setCurrentSection = setCurrentSection
        }
    }

    Crossfade(currentSection) { homeSection ->
        AlkaaHomeScaffold(
            homeSection = homeSection,
            navItems = navItems,
            actions = actions,
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AlkaaHomeScaffold(
    homeSection: HomeSection,
    navItems: ImmutableList<HomeSection>,
    actions: HomeActions,
) {
    Scaffold(
        topBar = {
            AlkaaTopBar(currentSection = homeSection)
        },
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
                Column(Modifier.fillMaxSize()) {
                    AlkaaContent(
                        homeSection = homeSection,
                        modifier = Modifier,
                        actions = actions,
                    )
                }
            }
        },
        bottomBar = {
            AlkaaBottomNav(
                currentSection = homeSection,
                onSectionSelect = actions.setCurrentSection,
                items = navItems,
            )
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
    actions: HomeActions,
    modifier: Modifier = Modifier,
) {
    when (homeSection) {
        HomeSection.Tasks -> {
            TaskListSection(
                modifier = modifier,
                onItemClick = actions.onTaskClick,
                onBottomShow = actions.onTaskSheetOpen,
            )
        }

        HomeSection.Search -> {}
        // SearchSection(modifier = modifier, onItemClick = actions.onTaskClick)

        HomeSection.Categories -> {
            CategoryListSection(
                modifier = modifier,
                onShowBottomSheet = actions.onCategorySheetOpen,
            )
        }

        HomeSection.Settings -> {}
        // PreferenceSection(
        //     modifier = modifier,
        //     onAboutClick = actions.onAboutClick,
        //     onTrackerClick = actions.onTrackerClick,
        //     onOpenSourceClick = actions.onOpenSourceClick,
        // )
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
