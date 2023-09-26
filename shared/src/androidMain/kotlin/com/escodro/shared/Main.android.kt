package com.escodro.shared

import androidx.compose.runtime.Composable

@Composable
fun MainView(onThemeUpdate: (isDarkTheme: Boolean) -> Unit = {}) =
    AlkaaMultiplatformApp(onThemeUpdate = onThemeUpdate)
