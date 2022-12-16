package com.escodro.preference.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.escodro.designsystem.components.AlkaaToolbar
import com.mikepenz.aboutlibraries.ui.compose.LibrariesContainer
import com.mikepenz.aboutlibraries.ui.compose.LibraryDefaults

/**
 * Alkaa open source licenses screen.
 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun OpenSource(onUpPress: () -> Unit, modifier: Modifier = Modifier) {
    Scaffold(
        topBar = { AlkaaToolbar(onUpPress = onUpPress) },
        content = { paddingValues -> OpenSourceContent(modifier = Modifier.padding(paddingValues)) },
        modifier = modifier,
    )
}

@Composable
private fun OpenSourceContent(modifier: Modifier = Modifier) {
    LibrariesContainer(
        modifier = modifier.fillMaxSize(),
        colors = LibraryDefaults.libraryColors(
            backgroundColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            badgeContentColor = MaterialTheme.colorScheme.onPrimary,
            badgeBackgroundColor = MaterialTheme.colorScheme.primary,
        ),
    )
}
