package com.escodro.shared

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.alkaa.appstate.rememberAlkaaAppState
import com.escodro.designsystem.AlkaaTheme
import com.escodro.navigation.NavigationAction
import com.escodro.shared.model.AppThemeOptions
import com.escodro.shared.navigation.AlkaaNavGraph
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun AlkaaMultiplatformApp(
    navigationAction: NavigationAction = NavigationAction.Home,
    modifier: Modifier = Modifier,
    onThemeUpdate: (isDarkTheme: Boolean) -> Unit = {},
) {
    val appState = rememberAlkaaAppState(windowSizeClass = calculateWindowSizeClass())
    val isDarkTheme = rememberIsDarkTheme()
    onThemeUpdate(isDarkTheme)
    AlkaaTheme(isDarkTheme = isDarkTheme) {
        AlkaaNavGraph(
            appState = appState,
            navigationAction = navigationAction,
            modifier = modifier,
        )
    }
}

@Composable
private fun rememberIsDarkTheme(viewModel: AppViewModel = koinInject()): Boolean {
    val isSystemDarkTheme = isSystemInDarkTheme()

    val theme by remember(viewModel) {
        viewModel.loadCurrentTheme()
    }.collectAsState(initial = AppThemeOptions.SYSTEM)

    val isDarkTheme = when (theme) {
        AppThemeOptions.LIGHT -> false
        AppThemeOptions.DARK -> true
        AppThemeOptions.SYSTEM -> isSystemDarkTheme
    }
    return isDarkTheme
}
