package com.escodro.alkaa.test

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.escodro.shared.di.initKoin
import org.koin.core.module.Module

/**
 * Run a test in the UI context, setting up the Koin modules and the Compose content.
 *
 * @param block the test to be executed
 */
@OptIn(ExperimentalTestApi::class)
fun uiTest(block: ComposeUiTest.() -> Unit) = runComposeUiTest {
    setContent {
        initKoin(appModule = module)
        RenderApp()
    }
    block()
}

/**
 * Koin module to provide the platform dependencies.

 */
expect val module: Module

@Composable
expect fun RenderApp()
