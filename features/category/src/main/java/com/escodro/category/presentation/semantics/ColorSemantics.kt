package com.escodro.category.presentation.semantics

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver

/**
 * Semantics key for color naming.
 */
val ColorKey = SemanticsPropertyKey<Color>("Color")

internal var SemanticsPropertyReceiver.color by ColorKey
