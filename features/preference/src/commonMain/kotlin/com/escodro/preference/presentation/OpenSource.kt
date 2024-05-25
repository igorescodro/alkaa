package com.escodro.preference.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.escodro.designsystem.components.AlkaaToolbar
import com.escodro.resources.Res
import com.mikepenz.aboutlibraries.ui.compose.LibrariesContainer
import com.mikepenz.aboutlibraries.ui.compose.LibraryDefaults
import org.jetbrains.compose.resources.ExperimentalResourceApi

/**
 * Alkaa open source licenses screen.
 */
@Composable
fun OpenSource(onUpPress: () -> Unit, modifier: Modifier = Modifier) {
    Scaffold(
        topBar = { AlkaaToolbar(onUpPress = onUpPress) },
        content = { paddingValues -> OpenSourceContent(modifier = Modifier.padding(paddingValues)) },
        modifier = modifier,
    )
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun OpenSourceContent(modifier: Modifier = Modifier) {
    var licenses by remember { mutableStateOf(ByteArray(0)) }

    LaunchedEffect(Unit) {
        licenses = Res.readBytes("files/aboutlibraries.json")
    }

    LibrariesContainer(
        aboutLibsJson = licenses.decodeToString(),
        modifier = modifier.fillMaxSize(),
        colors = LibraryDefaults.libraryColors(
            backgroundColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            badgeContentColor = MaterialTheme.colorScheme.onPrimary,
            badgeBackgroundColor = MaterialTheme.colorScheme.primary,
            dialogConfirmButtonColor = MaterialTheme.colorScheme.primary,
        ),
    )
}
