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
import com.escodro.preference.model.AppThemeOptions
import com.escodro.resources.Res
import com.escodro.resources.preference_title_features
import com.escodro.resources.preference_title_settings
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

/**
 * Alkaa Preference Section.
 *
 * @param modifier Compose modifier
 */
@Composable
fun PreferenceSection(
    onAboutClick: () -> Unit,
    onOpenSourceClick: () -> Unit,
    onTrackerClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PreferenceLoader(
        modifier = modifier,
        onAboutClick = onAboutClick,
        onTrackerClick = onTrackerClick,
        onOpenSourceClick = onOpenSourceClick,
    )
}

@Composable
private fun PreferenceLoader(
    onAboutClick: () -> Unit,
    onTrackerClick: () -> Unit,
    onOpenSourceClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PreferenceViewModel = koinInject(),
) {
    val theme by remember(viewModel) {
        viewModel.loadCurrentTheme()
    }.collectAsState(initial = AppThemeOptions.SYSTEM)

    PreferenceContent(
        onAboutClick = onAboutClick,
        onTrackerClick = onTrackerClick,
        onOpenSourceClick = onOpenSourceClick,
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
    theme: AppThemeOptions,
    onThemeUpdate: (AppThemeOptions) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
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
