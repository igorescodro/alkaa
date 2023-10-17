package com.escodro.shared

import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MainView(onThemeUpdate: (isDarkTheme: Boolean) -> Unit = {}) =
    AlkaaMultiplatformApp(modifier = Modifier.imePadding(), onThemeUpdate = onThemeUpdate)
