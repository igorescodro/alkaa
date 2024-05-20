package com.escodro.shared

import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.escodro.navigation.NavigationAction

/**
 * Main view of the application.
 *
 * @param navigationAction the navigation action to be consumed when the app is open
 * @param modifier the modifier to be applied to the view
 * @param onThemeUpdate the callback to be called when the theme is updated
 */
@Composable
fun MainView(
    navigationAction: NavigationAction,
    modifier: Modifier = Modifier,
    onThemeUpdate: (isDarkTheme: Boolean) -> Unit = {},
) = AlkaaMultiplatformApp(
    modifier = modifier.imePadding(),
    navigationAction = navigationAction,
    onThemeUpdate = onThemeUpdate,
)
