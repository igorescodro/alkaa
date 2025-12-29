package com.escodro.test.extension

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.onNodeWithTag

/**
 * Extension function to match a Material chip on the screen.
 *
 * @param text the text to be searched
 *
 * @return the semantics node interaction
 */
@OptIn(ExperimentalTestApi::class)
fun ComposeUiTest.onChip(text: String): SemanticsNodeInteraction =
    onNodeWithTag(text, useUnmergedTree = true)
