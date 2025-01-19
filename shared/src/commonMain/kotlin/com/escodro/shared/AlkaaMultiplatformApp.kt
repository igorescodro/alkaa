package com.escodro.shared

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.escodro.appstate.AppState
import com.escodro.appstate.rememberAlkaaAppState
import com.escodro.designsystem.AlkaaTheme
import com.escodro.home.presentation.Home
import com.escodro.shared.model.AppThemeOptions
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun AlkaaMultiplatformApp(
    modifier: Modifier = Modifier,
    appState: AppState = rememberAlkaaAppState(windowSizeClass = calculateWindowSizeClass()),
    onThemeUpdate: (isDarkTheme: Boolean) -> Unit = {},
) {
    val isDarkTheme = rememberIsDarkTheme()
    onThemeUpdate(isDarkTheme)
    AlkaaTheme(isDarkTheme = isDarkTheme) {
        Home(appState = appState)
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
