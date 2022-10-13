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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.escodro.designsystem.AlkaaTheme
import com.escodro.preference.R
import com.escodro.preference.model.AppThemeOptions
import org.koin.androidx.compose.getViewModel

/**
 * Alkaa Preference Section.
 *
 * @param modifier the decorator
 * @param onAboutClick function to be called when the about item is clicked
 * @param onTrackerClick function to be called when the tracker item is clicked
 * @param onOpenSourceClick function to be called when the open source item is clicked
 */
@Composable
fun PreferenceSection(
    modifier: Modifier = Modifier,
    onAboutClick: () -> Unit,
    onTrackerClick: () -> Unit,
    onOpenSourceClick: () -> Unit
) {
    PreferenceLoader(
        modifier = modifier,
        onAboutClick = onAboutClick,
        onTrackerClick = onTrackerClick,
        onOpenSourceClick = onOpenSourceClick
    )
}

@Composable
private fun PreferenceLoader(
    modifier: Modifier = Modifier,
    onAboutClick: () -> Unit,
    onTrackerClick: () -> Unit,
    onOpenSourceClick: () -> Unit,
    viewModel: PreferenceViewModel = getViewModel()
) {
    val theme by remember(viewModel) {
        viewModel.loadCurrentTheme()
    }.collectAsState(initial = AppThemeOptions.SYSTEM)

    PreferenceContent(
        modifier = modifier,
        onAboutClick = onAboutClick,
        onTrackerClick = onTrackerClick,
        onOpenSourceClick = onOpenSourceClick,
        theme = theme,
        onThemeUpdate = viewModel::updateTheme
    )
}

@Suppress("LongParameterList")
@Composable
internal fun PreferenceContent(
    modifier: Modifier = Modifier,
    onAboutClick: () -> Unit,
    onTrackerClick: () -> Unit,
    onOpenSourceClick: () -> Unit,
    theme: AppThemeOptions,
    onThemeUpdate: (AppThemeOptions) -> Unit
) {
    Column(modifier = modifier.fillMaxSize()) {
        PreferenceTitle(title = stringResource(id = R.string.preference_title_features))
        TrackerItem(onTrackerClick)
        Separator()
        PreferenceTitle(title = stringResource(id = R.string.preference_title_settings))
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
            .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.7F))
    )
}

@Suppress("UndocumentedPublicFunction")
@Preview
@Composable
fun PreferencePreview() {
    AlkaaTheme {
        PreferenceSection(onAboutClick = {}, onTrackerClick = {}, onOpenSourceClick = {})
    }
}
