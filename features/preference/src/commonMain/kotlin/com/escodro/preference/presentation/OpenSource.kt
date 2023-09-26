package com.escodro.preference.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.escodro.designsystem.components.AlkaaToolbar

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
    // LibrariesContainer( // TODO understand how to use this library in KMP
    //     modifier = modifier.fillMaxSize(),
    //     colors = LibraryDefaults.libraryColors(
    //         backgroundColor = MaterialTheme.colorScheme.background,
    //         contentColor = MaterialTheme.colorScheme.onBackground,
    //         badgeContentColor = MaterialTheme.colorScheme.onPrimary,
    //         badgeBackgroundColor = MaterialTheme.colorScheme.primary,
    //     ),
    // )
}
