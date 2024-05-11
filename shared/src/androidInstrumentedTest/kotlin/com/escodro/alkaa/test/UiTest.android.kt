package com.escodro.alkaa.test

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single<Context> { InstrumentationRegistry.getInstrumentation().targetContext }
    }
