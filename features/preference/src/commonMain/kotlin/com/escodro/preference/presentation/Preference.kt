package com.escodro.preference.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.escodro.navigation.AlkaaDestinations
import com.escodro.preference.model.AppThemeOptions
import com.escodro.preference.provider.TrackerProvider
import com.escodro.resources.MR
import dev.icerock.moko.resources.compose.stringResource
import org.koin.compose.koinInject

/**
 * Alkaa Preference Section.
 *
 * @param modifier Compose modifier
 */
@Composable
fun PreferenceSection(
    modifier: Modifier = Modifier,
) {
    val navigator = LocalNavigator.currentOrThrow
    PreferenceLoader(
        modifier = modifier,
        onAboutClick = {
            val screen = ScreenRegistry.get(AlkaaDestinations.Preferences.About)
            navigator.push(screen)
        },
        onTrackerClick = {
            val screen = ScreenRegistry.get(AlkaaDestinations.Preferences.Tracker)
            navigator.push(screen)
        },
        onOpenSourceClick = {
            val screen = ScreenRegistry.get(AlkaaDestinations.Preferences.OpenSource)
            navigator.push(screen)
        },
    )
}

@Composable
private fun PreferenceLoader(
    onAboutClick: () -> Unit,
    onTrackerClick: () -> Unit,
    onOpenSourceClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PreferenceViewModel = koinInject(),
    trackerProvider: TrackerProvider = koinInject(),
) {
    val theme by remember(viewModel) {
        viewModel.loadCurrentTheme()
    }.collectAsState(initial = AppThemeOptions.SYSTEM)

    PreferenceContent(
        onAboutClick = onAboutClick,
        onTrackerClick = onTrackerClick,
        onOpenSourceClick = onOpenSourceClick,
        isTrackerEnabled = trackerProvider.isEnabled,
        theme = theme,
        onThemeUpdate = viewModel::updateTheme,
        modifier = modifier,
    )
}

@Suppress("LongParameterList")
@Composable
internal fun PreferenceContent(
    onAboutClick: () -> Unit,
    onTrackerClick: () -> Unit,
    onOpenSourceClick: () -> Unit,
    isTrackerEnabled: Boolean,
    theme: AppThemeOptions,
    onThemeUpdate: (AppThemeOptions) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        if (isTrackerEnabled) {
            PreferenceTitle(title = stringResource(MR.strings.preference_title_features))
            TrackerItem(onTrackerClick)
            Separator()
        }
        PreferenceTitle(title = stringResource(MR.strings.preference_title_settings))
        ThemeItem(currentTheme = theme, onThemeUpdate = onThemeUpdate)
        AboutItem(onAboutClick = onAboutClick)
        OpenSourceLibraryItem(onOpenSourceClick = onOpenSourceClick)
        VersionItem()
    }
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
