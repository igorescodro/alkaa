package com.escodro.alkaa.presentation.home

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.escodro.alkaa.model.HomeSection
import com.escodro.category.presentation.CategoryListSection
import com.escodro.preference.presentation.PreferenceSection
import com.escodro.search.presentation.SearchSection
import com.escodro.task.presentation.list.TaskListSection
import com.escodro.theme.AlkaaTheme

/**
 * Alkaa Home screen.
 */
@Composable
fun Home(onTaskClicked: (Long) -> Unit, onAboutClicked: () -> Unit) {
    val (currentSection, setCurrentSection) = rememberSaveable { mutableStateOf(HomeSection.Tasks) }
    val navItems = HomeSection.values().toList()
    val homeModifier = Modifier.padding(bottom = 56.dp)

    val actions = HomeActions(
        onTaskClicked = onTaskClicked,
        onAboutClicked = onAboutClicked,
        setCurrentSection = setCurrentSection,
    )

    Crossfade(currentSection) { homeSection ->
        AlkaaHomeScaffold(
            homeSection = homeSection,
            modifier = homeModifier,
            navItems = navItems,
            actions = actions
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun AlkaaHomeScaffold(
    homeSection: HomeSection,
    modifier: Modifier,
    navItems: List<HomeSection>,
    actions: HomeActions
) {
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val (sheetContent, setSheetContent) = remember { mutableStateOf<@Composable () -> Unit>({ }) }
    AlkaaBottomSheetLayout(sheetState = sheetState, bottomSheetContent = sheetContent) {
        Scaffold(
            topBar = {
                AlkaaTopBar(currentSection = homeSection)
            },
            content = {
                AlkaaContent(homeSection, modifier, actions, setSheetContent, sheetState)
            },
            bottomBar = {
                AlkaaBottomNav(
                    currentSection = homeSection,
                    onSectionSelected = actions.setCurrentSection,
                    items = navItems
                )
            }
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun AlkaaContent(
    homeSection: HomeSection,
    modifier: Modifier,
    actions: HomeActions,
    setSheetContent: (@Composable() () -> Unit) -> Unit,
    sheetState: ModalBottomSheetState
) {
    when (homeSection) {
        HomeSection.Tasks ->
            TaskListSection(
                modifier = modifier,
                onItemClicked = actions.onTaskClicked,
                bottomSheetContent = setSheetContent,
                sheetState = sheetState
            )
        HomeSection.Search ->
            SearchSection(modifier = modifier, onItemClicked = actions.onTaskClicked)
        HomeSection.Categories ->
            CategoryListSection(modifier = modifier)
        HomeSection.Settings ->
            PreferenceSection(
                modifier = modifier,
                onAboutClicked = actions.onAboutClicked
            )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun AlkaaBottomSheetLayout(
    sheetState: ModalBottomSheetState,
    bottomSheetContent: @Composable() () -> Unit,
    content: @Composable() () -> Unit
) {
    val focusManager = LocalFocusManager.current
    DisposableEffect(sheetState.isVisible.not()) {
        onDispose { focusManager.clearFocus() }
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetBackgroundColor = Color.Transparent,
        sheetContent = {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(256.dp)
                    .padding(horizontal = 2.dp)
                    .clickable { /* consume click in the sheet background to avoid dismissal */ }
            ) {
                bottomSheetContent()
            }
        }
    ) {
        content()
    }
}

@Composable
private fun AlkaaTopBar(currentSection: HomeSection) {
    TopAppBar(backgroundColor = MaterialTheme.colors.background, elevation = 0.dp) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.h5,
                text = stringResource(currentSection.title)
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun AlkaaBottomNav(
    currentSection: HomeSection,
    onSectionSelected: (HomeSection) -> Unit,
    items: List<HomeSection>
) {
    BottomAppBar(backgroundColor = MaterialTheme.colors.background) {
        items.forEach { section ->
            val selected = section == currentSection
            val colorState = animateColorAsState(
                if (selected) {
                    MaterialTheme.colors.primary
                } else {
                    MaterialTheme.colors.onSecondary
                }
            )
            AlkaaBottomIcon(
                section = section,
                tint = colorState.value,
                onSectionSelected = onSectionSelected,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun AlkaaBottomIcon(
    section: HomeSection,
    tint: Color,
    onSectionSelected: (HomeSection) -> Unit,
    modifier: Modifier
) {
    val title = stringResource(section.title)
    IconButton(
        onClick = { onSectionSelected(section) },
        modifier = modifier
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
            onSectionSelected = {},
            items = HomeSection.values().toList()
        )
    }
}
