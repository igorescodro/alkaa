package com.escodro.designsystem.provider

import androidx.compose.material3.ColorScheme

internal class DesktopThemeProvider : ThemeProvider {
    override val isDynamicColorSupported: Boolean
        get() = false

    override val dynamicDarkColorScheme: ColorScheme
        get() = throw UnsupportedOperationException("Dynamic Theme not supported on Desktop")

    override val dynamicLightColorScheme: ColorScheme
        get() = throw UnsupportedOperationException("Dynamic Theme not supported on Desktop")
}
