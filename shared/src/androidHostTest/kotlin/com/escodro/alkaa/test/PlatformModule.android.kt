@file:Suppress("ktlint:standard:filename")

package com.escodro.alkaa.test

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single<Context> { ApplicationProvider.getApplicationContext<Context>() }
    }
