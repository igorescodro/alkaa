package com.escodro.preference.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.escodro.core.extension.getVersionName
import com.escodro.core.extension.openUrl
import com.escodro.designsystem.AlkaaTheme
import com.escodro.preference.R
import com.escodro.preference.model.AppThemeOptions
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import org.koin.androidx.compose.getViewModel
import java.util.Locale

/**
 * Alkaa Preference Section.
 *
 * @param modifier the decorator
 * @param onAboutClick function to be called when the about item is clicked
 * @param onTrackerClick function to be called when the tracker item is clicked
 */
@Composable
fun PreferenceSection(
    modifier: Modifier = Modifier,
    onAboutClick: () -> Unit,
    onTrackerClick: () -> Unit
) {
    PreferenceLoader(
        modifier = modifier,
        onAboutClick = onAboutClick,
        onTrackerClick = onTrackerClick
    )
}

@Composable
private fun PreferenceLoader(
    modifier: Modifier = Modifier,
    onAboutClick: () -> Unit,
    onTrackerClick: () -> Unit,
    viewModel: PreferenceViewModel = getViewModel()
) {
    val theme by remember(viewModel) {
        viewModel.loadCurrentTheme()
    }.collectAsState(initial = AppThemeOptions.SYSTEM)

    PreferenceContent(
        modifier = modifier,
        onAboutClick = onAboutClick,
        onTrackerClick = onTrackerClick,
        theme = theme,
        onThemeUpdate = viewModel::updateTheme
    )
}

@Composable
internal fun PreferenceContent(
    modifier: Modifier = Modifier,
    onAboutClick: () -> Unit,
    onTrackerClick: () -> Unit,
    theme: AppThemeOptions,
    onThemeUpdate: (AppThemeOptions) -> Unit
) {
    Column(modifier = modifier.fillMaxSize()) {
        PreferenceTitle(title = stringResource(id = R.string.preference_title_features))
        TrackerItem(onTrackerClick)
        Separator()
        PreferenceTitle(title = stringResource(id = R.string.preference_title_settings))
        ThemeItem(currentTheme = theme, onThemeUpdate = onThemeUpdate)
        AboutItem(onAboutClick)
        VersionItem()
    }
}

@Composable
private fun PreferenceTitle(title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 32.dp, end = 16.dp)
            .height(32.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = title.uppercase(Locale.getDefault()),
            style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colors.primary
        )
    }
}

@Composable
private fun TrackerItem(onTrackerClick: () -> Unit) {
    PreferenceItem(
        title = stringResource(id = R.string.preference_title_tracker),
        onItemClick = onTrackerClick
    )
}

@Composable
@Suppress("MagicNumber")
private fun VersionItem() {
    val title = stringResource(id = R.string.preference_title_version)
    val context = LocalContext.current
    val version = context.getVersionName()
    var numberOfClicks by remember { mutableStateOf(0) }
    val onClick = {
        if (++numberOfClicks == 7) {
            context.openUrl(EasterEggUrl)
        }
    }

    LaunchedEffect(Unit) {
        snapshotFlow { numberOfClicks }
            .filter { it > 0 }
            .collectLatest {
                delay(1_000)
                numberOfClicks = 0
            }
    }

    PreferenceItem(title = title, description = version, onItemClick = onClick)
}

@Composable
private fun AboutItem(onAboutClick: () -> Unit) {
    PreferenceItem(
        title = stringResource(id = R.string.preference_title_about),
        onItemClick = onAboutClick
    )
}

@Composable
private fun ThemeItem(
    currentTheme: AppThemeOptions,
    onThemeUpdate: (AppThemeOptions) -> Unit
) {
    var isDialogOpen by remember { mutableStateOf(false) }

    PreferenceItem(
        title = stringResource(id = R.string.preference_title_app_theme),
        description = stringResource(id = currentTheme.titleRes),
        onItemClick = { isDialogOpen = true }
    )

    AppThemeDialog(
        isDialogOpen = isDialogOpen,
        onDismissRequest = { isDialogOpen = false },
        currentTheme = currentTheme,
        onThemeUpdate = onThemeUpdate
    )
}

@Composable
private fun Separator() {
    Spacer(
        modifier = Modifier
            .padding(bottom = 8.dp)
            .height(1.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colors.onSecondary.copy(alpha = 0.7F))
    )
}

private const val EasterEggUrl = "https://www.youtube.com/watch?v=dQw4w9WgXcQ"

@Suppress("UndocumentedPublicFunction")
@Preview
@Composable
fun PreferencePreview() {
    AlkaaTheme {
        PreferenceSection(onAboutClick = {}, onTrackerClick = {})
    }
}
