package com.escodro.shared

import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Main view of the application.
 *
 * @param modifier the modifier to be applied to the view
 * @param onThemeUpdate the callback to be called when the theme is updated
 */
@Composable
fun MainView(
    modifier: Modifier = Modifier,
    onThemeUpdate: (isDarkTheme: Boolean) -> Unit = {},
) = AlkaaMultiplatformApp(
    modifier = modifier.imePadding(),
    onThemeUpdate = onThemeUpdate,
)
