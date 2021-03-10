package com.escodro.category.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Alkaa Category List Section.
 *
 * @param modifier the decorator
 */
@Composable
fun CategoryListSection(modifier: Modifier) {
    CategoryListLoader(modifier)
}

@Composable
private fun CategoryListLoader(modifier: Modifier) {
    CategoryListScaffold(modifier)
}

@Composable
private fun CategoryListScaffold(modifier: Modifier) {
    Scaffold(modifier = modifier.fillMaxSize()) {
    }
}
