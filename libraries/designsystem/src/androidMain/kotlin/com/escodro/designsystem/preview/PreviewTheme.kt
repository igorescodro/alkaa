package com.escodro.designsystem.preview

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import com.escodro.designsystem.AlkaaDarkColorScheme
import com.escodro.designsystem.AlkaaLightColorScheme
import com.escodro.designsystem.AlkaaTheme
import com.escodro.designsystem.provider.ThemeProvider

@Composable
fun PreviewTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    isDynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    AlkaaTheme(
        isDarkTheme = isDarkTheme,
        isDynamicColor = isDynamicColor,
        themeProvider = PreviewThemeProvider(),
    ) {
        content()
    }
}

private class PreviewThemeProvider : ThemeProvider {

    override val isDynamicColorSupported: Boolean = false

    override val dynamicDarkColorScheme: ColorScheme = AlkaaDarkColorScheme

    override val dynamicLightColorScheme: ColorScheme = AlkaaLightColorScheme
}
