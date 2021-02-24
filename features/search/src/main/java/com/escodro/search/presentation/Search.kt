package com.escodro.search.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.escodro.theme.AlkaaTheme

/**
 * Alkaa Search Section.
 *
 * @param modifier the decorator
 */
@Composable
fun SearchSection(modifier: Modifier = Modifier) {
    SearchLoader(modifier = modifier)
}

@Composable
private fun SearchLoader(modifier: Modifier = Modifier) {
    SearchScaffold(modifier = modifier)
}

@Composable
internal fun SearchScaffold(modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        backgroundColor = MaterialTheme.colors.background
    ) {
        Text("This is the search")
    }
}

@Suppress("UndocumentedPublicFunction")
@Preview
@Composable
fun SearchScaffoldPreview() {
    AlkaaTheme {
        SearchScaffold(
            modifier = Modifier
        )
    }
}
