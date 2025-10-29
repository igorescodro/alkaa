package com.escodro.preference.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.escodro.designsystem.components.dialog.AlkaaDialog
import com.escodro.designsystem.components.dialog.DialogArguments
import com.escodro.designsystem.components.topbar.AlkaaToolbar
import com.escodro.resources.Res
import com.escodro.resources.default_ok
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.entity.Library
import com.mikepenz.aboutlibraries.ui.compose.util.author
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

/**
 * Alkaa open source licenses screen.
 */
@Composable
fun OpenSource(
    isSinglePane: Boolean,
    onUpPress: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            AlkaaToolbar(
                isSinglePane = isSinglePane,
                onUpPress = onUpPress,
            )
        },
        content = { paddingValues -> OpenSourceContent(modifier = Modifier.padding(paddingValues)) },
        modifier = modifier,
    )
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun OpenSourceContent(modifier: Modifier = Modifier) {
    var licenses by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var selectedLibrary by remember { mutableStateOf<Library?>(null) }

    LaunchedEffect(Unit) {
        licenses = Res.readBytes("files/aboutlibraries.json").decodeToString()
    }

    val libraries: Libs = Libs.Builder().withJson(licenses).build()

    Box(modifier = modifier) {
        LazyColumn {
            items(items = libraries.libraries) { library ->
                OpenSourceItem(
                    library = library,
                    onClick = {
                        selectedLibrary = library
                        showDialog = true
                    },
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                )
            }
        }

        if (selectedLibrary != null && selectedLibrary.isLicenseAvailable()) {
            AlkaaDialog(
                arguments = DialogArguments(
                    title = selectedLibrary?.name ?: "",
                    text = selectedLibrary?.licenses?.firstOrNull()?.licenseContent ?: "",
                    confirmText = stringResource(Res.string.default_ok),
                    onConfirmAction = { showDialog = false },
                ),
                isDialogOpen = showDialog,
                onDismissRequest = { showDialog = false },
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f)
                    .padding(vertical = 16.dp),
            )
        }
    }
}

@Composable
private fun OpenSourceItem(library: Library, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = library.name,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = "${library.artifactVersion}",
                style = MaterialTheme.typography.bodyLarge,
            )
        }
        Text(
            text = library.author,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 4.dp),
        )
        SuggestionChip(
            label = {
                Text(
                    text = library.licenses.firstOrNull()?.name ?: "",
                    style = MaterialTheme.typography.bodySmall,
                )
            },
            shape = MaterialTheme.shapes.extraLarge,
            colors = SuggestionChipDefaults
                .suggestionChipColors()
                .copy(containerColor = MaterialTheme.colorScheme.secondaryContainer),
            onClick = { onClick() },
        )
    }
}

private fun Library?.isLicenseAvailable(): Boolean =
    this
        ?.licenses
        ?.firstOrNull()
        ?.licenseContent
        ?.isBlank() == false
