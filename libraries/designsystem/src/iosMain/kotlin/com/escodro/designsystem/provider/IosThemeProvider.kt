package com.escodro.designsystem.provider

import androidx.compose.material3.ColorScheme

internal class IosThemeProvider : ThemeProvider {
    override val isDynamicColorSupported: Boolean
        get() = false

    override val dynamicDarkColorScheme: ColorScheme
        get() = throw UnsupportedOperationException("Dynamic Theme not supported on iOS")

    override val dynamicLightColorScheme: ColorScheme
        get() = throw UnsupportedOperationException("Dynamic Theme not supported on iOS")
}
