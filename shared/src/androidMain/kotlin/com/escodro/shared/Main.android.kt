package com.escodro.shared

import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.escodro.navigation.NavigationAction

/**
 * Main view of the application.
 *
 * @param navigationAction the navigation action to be consumed when the app is open
 * @param onThemeUpdate the callback to be called when the theme is updated
 */
@Composable
fun MainView(
    navigationAction: NavigationAction,
    onThemeUpdate: (isDarkTheme: Boolean) -> Unit = {},
) = AlkaaMultiplatformApp(
    navigationAction = navigationAction,
    modifier = Modifier.imePadding(),
    onThemeUpdate = onThemeUpdate,
)
