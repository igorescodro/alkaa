package com.escodro.shared

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.escodro.designsystem.AlkaaTheme
import com.escodro.shared.model.AppThemeOptions
import com.escodro.shared.navigation.AlkaaNavGraph
import org.koin.compose.koinInject

@Composable
fun AlkaaMultiplatformApp(
    modifier: Modifier = Modifier,
    onThemeUpdate: (isDarkTheme: Boolean) -> Unit = {},
) {
    val isDarkTheme = rememberIsDarkTheme()
    onThemeUpdate(isDarkTheme)
    AlkaaTheme(isDarkTheme = isDarkTheme) {
        AlkaaNavGraph(modifier)
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
