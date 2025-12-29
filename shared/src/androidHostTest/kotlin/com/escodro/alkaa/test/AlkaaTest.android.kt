@file:Suppress("ktlint:standard:filename")

package com.escodro.alkaa.test

import android.content.ContentProvider
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.testing.WorkManagerTestInitHelper
import org.junit.runner.RunWith
import org.koin.core.module.Module
import org.koin.dsl.module
import org.robolectric.Robolectric
import org.robolectric.annotation.Config

@Suppress("AbstractClassCanBeConcreteClass")
@RunWith(AndroidJUnit4::class)
@Config(sdk = [34])
actual abstract class AlkaaTest actual constructor() {
    init {
        setupAndroidContextProvider()
        setupWorkManager()
    }

    private fun setupWorkManager() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        WorkManagerTestInitHelper.initializeTestWorkManager(context)
    }

    private fun setupAndroidContextProvider() {
        val type = findAndroidContextProvider() ?: return
        Robolectric.setupContentProvider(type)
    }

    private fun findAndroidContextProvider(): Class<ContentProvider>? {
        val providerClassName = "org.jetbrains.compose.resources.AndroidContextProvider"
        return try {
            @Suppress("UNCHECKED_CAST")
            Class.forName(providerClassName) as Class<ContentProvider>
        } catch (_: ClassNotFoundException) {
            null
        }
    }
}

actual val platformModule: Module
    get() = module {
        single<Context> { ApplicationProvider.getApplicationContext<Context>() }
    }
