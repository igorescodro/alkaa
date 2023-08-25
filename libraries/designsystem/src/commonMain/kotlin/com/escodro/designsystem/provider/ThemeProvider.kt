package com.escodro.designsystem.provider

import androidx.compose.material3.ColorScheme

/**
 * Provides the theme colors for each platform.
 */
interface ThemeProvider {

    /**
     * Returns if the dynamic color is supported by the platform.
     */
    val isDynamicColorSupported: Boolean

    /**
     * Returns the [ColorScheme] for the dark theme.
     */
    val dynamicDarkColorScheme: ColorScheme

    /**
     * Returns the [ColorScheme] for the light theme.
     */
    val dynamicLightColorScheme: ColorScheme
}
