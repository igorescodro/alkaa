package com.escodro.preference.presentation

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.escodro.theme.AlkaaTheme

/**
 * Alkaa about screen.
 */
@Composable
fun About() {
    Scaffold {
    }
}

@Suppress("UndocumentedPublicFunction")
@Preview
@Composable
fun AboutPreview() {
    AlkaaTheme {
        PreferenceSection(onAboutClicked = {})
    }
}
