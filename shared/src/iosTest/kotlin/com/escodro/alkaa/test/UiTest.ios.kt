package com.escodro.alkaa.test

import androidx.compose.runtime.Composable
import com.escodro.shared.MainViewController
import org.koin.core.module.Module
import org.koin.dsl.module

actual val module: Module
    get() = module { }

@Composable
actual fun RenderApp() {
    MainViewController()
}
