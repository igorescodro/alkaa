package com.escodro.test.extension

import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.junit4.ComposeContentTestRule

/**
 * Waits until the given matcher satisfied or until the [timeoutMillis] is reached.
 *
 * See: https://medium.com/androiddevelopers/alternatives-to-idling-resources-in-compose-tests-8ae71f9fc473
 */
fun ComposeContentTestRule.waitUntilExists(
    matcher: SemanticsMatcher,
    timeoutMillis: Long = 3_000L
) = waitUntilNodeCount(matcher = matcher, count = 1, timeoutMillis = timeoutMillis)

fun ComposeContentTestRule.waitUntilNotExists(
    matcher: SemanticsMatcher,
    timeoutMillis: Long = 3_000L
) = waitUntilNodeCount(matcher = matcher, count = 0, timeoutMillis = timeoutMillis)

private fun ComposeContentTestRule.waitUntilNodeCount(
    matcher: SemanticsMatcher,
    count: Int,
    timeoutMillis: Long = 3_000L
) = waitUntil(timeoutMillis) {
    onAllNodes(matcher).fetchSemanticsNodes().size == count
}
