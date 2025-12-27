package com.escodro.preference.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.escodro.preference.model.AppThemeOptions
import com.escodro.resources.Res
import com.escodro.resources.preference_title_features
import com.escodro.resources.preference_title_settings
import com.escodro.tracker.presentation.TrackerScreen
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

/**
 * Alkaa Preference Section.
 *
 * @param modifier Compose modifier
 */
@Composable
fun PreferenceSection(
    isSinglePane: Boolean,
    onAboutClick: () -> Unit,
    onOpenSourceClick: () -> Unit,
    onTrackerClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PreferenceLoader(
        isSinglePane = isSinglePane,
        modifier = modifier,
        onAboutClick = onAboutClick,
        onTrackerClick = onTrackerClick,
        onOpenSourceClick = onOpenSourceClick,
    )
}

@Composable
private fun PreferenceLoader(
    isSinglePane: Boolean,
    onAboutClick: () -> Unit,
    onTrackerClick: () -> Unit,
    onOpenSourceClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PreferenceViewModel = koinInject(),
) {
    val theme by remember(viewModel) {
        viewModel.loadCurrentTheme()
    }.collectAsState(initial = AppThemeOptions.SYSTEM)

    if (isSinglePane) {
        PreferenceContent(
            onAboutClick = onAboutClick,
            onTrackerClick = onTrackerClick,
            onOpenSourceClick = onOpenSourceClick,
            theme = theme,
            onThemeUpdate = viewModel::updateTheme,
            modifier = modifier,
        )
    } else {
        AdaptivePreferenceScaffold(
            theme = theme,
            onThemeUpdate = viewModel::updateTheme,
            modifier = modifier,
        )
    }
}

@Suppress("LongParameterList")
@Composable
internal fun PreferenceContent(
    onAboutClick: () -> Unit,
    onTrackerClick: () -> Unit,
    onOpenSourceClick: () -> Unit,
    theme: AppThemeOptions,
    onThemeUpdate: (AppThemeOptions) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        PreferenceTitle(title = stringResource(Res.string.preference_title_features))
        TrackerItem(onTrackerClick)
        Separator()
        PreferenceTitle(title = stringResource(Res.string.preference_title_settings))
        ThemeItem(currentTheme = theme, onThemeUpdate = onThemeUpdate)
        AboutItem(onAboutClick = onAboutClick)
        OpenSourceLibraryItem(onOpenSourceClick = onOpenSourceClick)
        VersionItem()
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun AdaptivePreferenceScaffold(
    theme: AppThemeOptions,
    onThemeUpdate: (AppThemeOptions) -> Unit,
    modifier: Modifier = Modifier,
) {
    val navigator: ThreePaneScaffoldNavigator<PreferenceItem> =
        rememberListDetailPaneScaffoldNavigator<PreferenceItem>()
    val coroutineScope = rememberCoroutineScope()

    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            PreferenceContent(
                onTrackerClick = {
                    coroutineScope.launch {
                        navigator.navigateTo(
                            ListDetailPaneScaffoldRole.Detail,
                            PreferenceItem.TRACKER,
                        )
                    }
                },
                onAboutClick = {
                    coroutineScope.launch {
                        navigator.navigateTo(
                            ListDetailPaneScaffoldRole.Detail,
                            PreferenceItem.ABOUT,
                        )
                    }
                },
                onOpenSourceClick = {
                    coroutineScope.launch {
                        navigator.navigateTo(
                            ListDetailPaneScaffoldRole.Detail,
                            PreferenceItem.OPEN_SOURCE,
                        )
                    }
                },
                theme = theme,
                onThemeUpdate = onThemeUpdate,
                modifier = modifier,
            )
        },
        detailPane = {
            val detail = navigator.currentDestination?.contentKey
            when (detail) {
                PreferenceItem.TRACKER -> {
                    TrackerScreen(
                        onUpPress = { coroutineScope.launch { navigator.navigateBack() } },
                    )
                }

                PreferenceItem.ABOUT -> {
                    AboutScreen(
                        isSinglePane = false,
                        onUpPress = { coroutineScope.launch { navigator.navigateBack() } },
                    )
                }

                PreferenceItem.OPEN_SOURCE -> {
                    OpenSource(
                        isSinglePane = false,
                        onUpPress = { coroutineScope.launch { navigator.navigateBack() } },
                    )
                }

                else -> {
                    // Render nothing
                }
            }
        },
    )
}

@Composable
private fun Separator() {
    Spacer(
        modifier = Modifier
            .padding(bottom = 8.dp)
            .height(1.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.7F)),
    )
}

/**
 * Enum used to represent the preference item with the adaptive navigation.
 */
private enum class PreferenceItem {
    TRACKER,
    ABOUT,
    OPEN_SOURCE,
}
