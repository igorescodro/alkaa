package com.escodro.shared

import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.ui.platform.AccessibilityDebugLogger
import androidx.compose.ui.platform.AccessibilitySyncOptions
import androidx.compose.ui.window.ComposeUIViewController

@OptIn(ExperimentalComposeApi::class)
@Suppress("Unused", "FunctionName")
fun MainViewController() = ComposeUIViewController(
    configure = {
        accessibilitySyncOptions = AccessibilitySyncOptions.Always(AccessibilityDebugLoggerImpl())
    }
) {
    AlkaaMultiplatformApp()
}

@OptIn(ExperimentalComposeApi::class)
private class AccessibilityDebugLoggerImpl : AccessibilityDebugLogger {
    override fun log(message: Any?) {
        if (message == null) {
            println()
        } else {
            println(message)
        }
    }
}
