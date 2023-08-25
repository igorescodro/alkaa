package com.escodro.designsystem.provider

import android.content.Context
import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme

@Suppress("NewApi")
internal class AndroidThemeProvider(private val context: Context) : ThemeProvider {

    @get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
    override val isDynamicColorSupported: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    override val dynamicDarkColorScheme: ColorScheme
        get() = dynamicDarkColorScheme(context)

    override val dynamicLightColorScheme: ColorScheme
        get() = dynamicLightColorScheme(context)
}
