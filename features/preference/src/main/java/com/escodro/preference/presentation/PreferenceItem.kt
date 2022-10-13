package com.escodro.preference.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.dp
import com.escodro.core.extension.getVersionName
import com.escodro.core.extension.openUrl
import com.escodro.preference.R
import com.escodro.preference.model.AppThemeOptions
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import java.util.Locale

@Composable
internal fun PreferenceItem(
    title: String,
    description: String? = null,
    onItemClick: () -> Unit = { }
) {
    Column(
        modifier = Modifier
            .clickable { onItemClick() }
            .padding(start = 32.dp, top = 8.dp, end = 16.dp, bottom = 8.dp)
            .fillMaxWidth()
            .height(48.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium
        )
        if (description != null) {
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
internal fun PreferenceTitle(title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 32.dp, end = 16.dp)
            .height(32.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = title.uppercase(Locale.getDefault()),
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
internal fun TrackerItem(onTrackerClick: () -> Unit) {
    PreferenceItem(
        title = stringResource(id = R.string.preference_title_tracker),
        onItemClick = onTrackerClick
    )
}

@Composable
internal fun ThemeItem(
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
internal fun AboutItem(onAboutClick: () -> Unit) {
    PreferenceItem(
        title = stringResource(id = R.string.preference_title_about),
        onItemClick = onAboutClick
    )
}

@Composable
internal fun OpenSourceLibraryItem(onOpenSourceClick: () -> Unit) {
    PreferenceItem(
        title = stringResource(id = R.string.preference_title_open_source),
        onItemClick = onOpenSourceClick
    )
}

@Composable
@Suppress("MagicNumber")
internal fun VersionItem() {
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

private const val EasterEggUrl = "https://www.youtube.com/watch?v=dQw4w9WgXcQ"
