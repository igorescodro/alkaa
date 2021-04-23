package com.escodro.test

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assert

/**
 * Assert the node's text line equals the given lines count.
 * Throws [AssertionError] if the node's value is not equal to `value`, or if the node has no value.
 *
 * @param lines number of lines
 */
fun SemanticsNodeInteraction.assertLines(lines: Int): SemanticsNodeInteraction =
    assert(hasLines(lines))

/**
 * Assert if the node is with the "Checked" value.
 */
fun SemanticsNodeInteraction.assertIsChecked(): SemanticsNodeInteraction =
    assert(SemanticsMatcher.expectValue(SemanticsProperties.StateDescription, "Checked"))

/**
 * Assert if the node is with the "Unchecked" value.
 */
fun SemanticsNodeInteraction.assertIsUnchecked(): SemanticsNodeInteraction =
    assert(SemanticsMatcher.expectValue(SemanticsProperties.StateDescription, "Unchecked"))
