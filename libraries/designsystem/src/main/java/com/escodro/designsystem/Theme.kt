package com.escodro.designsystem

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val AlkaaDarkColorScheme = darkColorScheme(
    primary = blue700
)

private val AlkaaLightColorScheme = lightColorScheme(
    primary = blue700
)

/**
 * Alkaa main theme.
 *
 * @param isDarkTheme indicates if the application is in dark theme mode.
 * @param isDynamicColor
 * @param content composable function
 */
@Composable
fun AlkaaTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    isDynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val dynamicColor = isDynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val colorScheme = when {
        dynamicColor && isDarkTheme -> dynamicDarkColorScheme(LocalContext.current)
        dynamicColor && !isDarkTheme -> dynamicLightColorScheme(LocalContext.current)
        isDarkTheme -> AlkaaDarkColorScheme
        else -> AlkaaLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
