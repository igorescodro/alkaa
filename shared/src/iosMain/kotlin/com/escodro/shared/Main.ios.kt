package com.escodro.shared

import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.ui.platform.AccessibilitySyncOptions
import androidx.compose.ui.window.ComposeUIViewController

@OptIn(ExperimentalComposeApi::class)
@Suppress("Unused", "FunctionName")
fun MainViewController() = ComposeUIViewController(
    configure = {
        accessibilitySyncOptions = AccessibilitySyncOptions.WhenRequiredByAccessibilityServices
    },
) {
    AlkaaMultiplatformApp()
}
