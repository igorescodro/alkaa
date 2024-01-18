@file:Suppress("Filename")

package com.escodro.androidtest.extension

import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.hasText

/**
 * Finds a semantic node containing a Material Chip based on the label text.
 */
fun SemanticsNodeInteractionsProvider.onChip(labelText: String) = onNode(
    withRole(Role.Checkbox).and(hasText(labelText)),
)

private fun withRole(role: Role): SemanticsMatcher {
    return SemanticsMatcher("${SemanticsProperties.Role.name} contains '$role'") {
        it.config.getOrNull(SemanticsProperties.Role) == role
    }
}
