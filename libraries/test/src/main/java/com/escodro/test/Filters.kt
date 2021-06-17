package com.escodro.test

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher

/**
 * Returns whether the node's text lines matches exactly to the given lines count.
 *
 * @param lines number of lines
 */
fun hasLines(lines: Int): SemanticsMatcher =
    SemanticsMatcher(
        description = "Has $lines lines"
    ) { node ->
        node.config.getOrNull(SemanticsProperties.Text)?.get(0)?.text?.lines()?.size == lines
    }
