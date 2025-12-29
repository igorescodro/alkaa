package com.escodro.preference.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.escodro.designsystem.components.item.PreferenceItem
import com.escodro.preference.model.AppThemeOptions
import com.escodro.preference.provider.AppInfoProvider
import com.escodro.resources.Res
import com.escodro.resources.preference_title_about
import com.escodro.resources.preference_title_app_theme
import com.escodro.resources.preference_title_open_source
import com.escodro.resources.preference_title_tracker
import com.escodro.resources.preference_title_version
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
internal fun PreferenceTitle(title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 32.dp, end = 16.dp)
            .height(32.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        Text(
            text = title.uppercase(),
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
internal fun TrackerItem(onTrackerClick: () -> Unit) {
    PreferenceItem(
        title = stringResource(Res.string.preference_title_tracker),
        onItemClick = onTrackerClick,
    )
}

@Composable
internal fun ThemeItem(
    currentTheme: AppThemeOptions,
    onThemeUpdate: (AppThemeOptions) -> Unit,
) {
    var isDialogOpen by remember { mutableStateOf(false) }

    PreferenceItem(
        title = stringResource(Res.string.preference_title_app_theme),
        description = stringResource(currentTheme.titleRes),
        onItemClick = { isDialogOpen = true },
    )

    AppThemeDialog(
        isDialogOpen = isDialogOpen,
        onDismissRequest = { isDialogOpen = false },
        currentTheme = currentTheme,
        onThemeUpdate = onThemeUpdate,
    )
}

@Composable
internal fun AboutItem(onAboutClick: () -> Unit) {
    PreferenceItem(
        title = stringResource(Res.string.preference_title_about),
        onItemClick = onAboutClick,
    )
}

@Composable
internal fun OpenSourceLibraryItem(onOpenSourceClick: () -> Unit) {
    PreferenceItem(
        title = stringResource(Res.string.preference_title_open_source),
        onItemClick = onOpenSourceClick,
    )
}

@Composable
@Suppress("MagicNumber")
internal fun VersionItem(appInfoProvider: AppInfoProvider = koinInject()) {
    val title = stringResource(Res.string.preference_title_version)
    val version = appInfoProvider.getAppVersion()
    var numberOfClicks by remember { mutableIntStateOf(0) }
    val uriHandler = LocalUriHandler.current

    val onClick = {
        if (++numberOfClicks == 7) {
            uriHandler.openUri(EasterEggUrl)
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

private const val EasterEggUrl = "https://www.youtube.com/watch?v=dQw4w9WgXcQ"
