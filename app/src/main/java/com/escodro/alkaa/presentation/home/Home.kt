package com.escodro.alkaa.presentation.home

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.escodro.alkaa.model.HomeSection
import com.escodro.category.presentation.list.CategoryListSection
import com.escodro.designsystem.AlkaaTheme
import com.escodro.preference.presentation.PreferenceSection
import com.escodro.search.presentation.SearchSection
import com.escodro.task.presentation.list.TaskListSection
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
    val navItems = HomeSection.values().toList().toImmutableList()

    val actions = HomeActions(
        onTaskClick = onTaskClick,
        onAboutClick = onAboutClick,
        onTrackerClick = onTrackerClick,
        onOpenSourceClick = onOpenSourceClick,
        onTaskSheetOpen = onTaskSheetOpen,
        onCategorySheetOpen = onCategorySheetOpen,
        setCurrentSection = setCurrentSection,
    )

    Crossfade(currentSection) { homeSection ->
        AlkaaHomeScaffold(
            homeSection = homeSection,
            navItems = navItems,
            actions = actions,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
        content = { paddingValues ->
            AlkaaContent(
                homeSection = homeSection,
                modifier = Modifier.padding(paddingValues),
                actions = actions,
            )
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
private fun AlkaaContent(
    homeSection: HomeSection,
    actions: HomeActions,
    modifier: Modifier = Modifier,
) {
    when (homeSection) {
        HomeSection.Tasks ->
            TaskListSection(
                modifier = modifier,
                onItemClick = actions.onTaskClick,
                onBottomShow = actions.onTaskSheetOpen,
            )
        HomeSection.Search ->
            SearchSection(modifier = modifier, onItemClick = actions.onTaskClick)
        HomeSection.Categories ->
            CategoryListSection(
                modifier = modifier,
                onShowBottomSheet = actions.onCategorySheetOpen,

            )
        HomeSection.Settings ->
            PreferenceSection(
                modifier = modifier,
                onAboutClick = actions.onAboutClick,
                onTrackerClick = actions.onTrackerClick,
                onOpenSourceClick = actions.onOpenSourceClick,
            )
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
            val colorState = animateColorAsState(
                if (selected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.outline
                },
            )
            AlkaaBottomIcon(
                section = section,
                tint = colorState.value,
                onSectionSelect = onSectionSelect,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun AlkaaBottomIcon(
    section: HomeSection,
    tint: Color,
    onSectionSelect: (HomeSection) -> Unit,
    modifier: Modifier = Modifier,
) {
    val title = stringResource(section.title)
    IconButton(
        onClick = { onSectionSelect(section) },
        modifier = modifier,
    ) {
        Icon(imageVector = section.icon, tint = tint, contentDescription = title)
    }
}

@Suppress("UndocumentedPublicFunction")
@Preview(showBackground = true)
@Composable
fun AlkaaTopBarPreview() {
    AlkaaTheme {
        AlkaaTopBar(HomeSection.Tasks)
    }
}

@Suppress("UndocumentedPublicFunction")
@Preview(showBackground = true)
@Composable
fun AlkaaBottomNavPreview() {
    AlkaaTheme {
        AlkaaBottomNav(
            currentSection = HomeSection.Tasks,
            onSectionSelect = {},
            items = HomeSection.values().toList().toImmutableList(),
        )
    }
}
