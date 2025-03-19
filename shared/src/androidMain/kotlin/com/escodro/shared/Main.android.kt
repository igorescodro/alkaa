package com.escodro.shared

import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Main view of the application.
 *
 * @param modifier the modifier to be applied to the view
 */
@Composable
fun MainView(
    modifier: Modifier = Modifier,
) = AlkaaMultiplatformApp(
    modifier = modifier.imePadding(),
)
