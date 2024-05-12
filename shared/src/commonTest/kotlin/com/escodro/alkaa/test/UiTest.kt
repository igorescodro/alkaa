package com.escodro.alkaa.test

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.escodro.alkaa.AppStateFake
import com.escodro.shared.AlkaaMultiplatformApp
import org.koin.compose.KoinContext
import org.koin.core.module.Module

/**
 * Run a test in the UI context, setting up the Koin modules and the Compose content.
 *
 * @param block the test to be executed
 */
@OptIn(ExperimentalTestApi::class)
fun uiTest(block: ComposeUiTest.() -> Unit) = runComposeUiTest {
    setContent {
        KoinContext { AlkaaMultiplatformApp(appState = AppStateFake()) }
    }
    block()
}

/**
 * Koin module to provide the platform dependencies.

 */
expect val platformModule: Module
