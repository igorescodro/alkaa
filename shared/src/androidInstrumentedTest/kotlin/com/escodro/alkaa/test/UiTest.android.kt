package com.escodro.alkaa.test

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.test.platform.app.InstrumentationRegistry
import com.escodro.shared.AlkaaMultiplatformApp
import org.koin.core.module.Module
import org.koin.dsl.module

actual val module: Module
    get() = module {
        single<Context> { InstrumentationRegistry.getInstrumentation().targetContext }
    }

@Composable
actual fun RenderApp() {
    AlkaaMultiplatformApp()
}
