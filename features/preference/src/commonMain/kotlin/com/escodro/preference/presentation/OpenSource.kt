package com.escodro.preference.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.escodro.designsystem.components.AlkaaToolbar
import com.escodro.resources.MR
import com.mikepenz.aboutlibraries.ui.compose.LibrariesContainer
import com.mikepenz.aboutlibraries.ui.compose.LibraryDefaults
import dev.icerock.moko.resources.compose.readTextAsState

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

@Composable
private fun OpenSourceContent(modifier: Modifier = Modifier) {
    val licenses by MR.files.aboutlibraries.readTextAsState()

    LibrariesContainer(
        aboutLibsJson = licenses ?: "",
        modifier = modifier.fillMaxSize(),
        colors = LibraryDefaults.libraryColors(
            backgroundColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            badgeContentColor = MaterialTheme.colorScheme.onPrimary,
            badgeBackgroundColor = MaterialTheme.colorScheme.primary,
        ),
    )
}
