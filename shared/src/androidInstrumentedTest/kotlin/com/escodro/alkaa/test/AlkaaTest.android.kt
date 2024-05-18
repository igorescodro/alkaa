@file:Suppress("ktlint:standard:filename")

package com.escodro.alkaa.test

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import org.koin.core.module.Module
import org.koin.dsl.module

actual class PlatformAnimation actual constructor() {

    private val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    actual fun disable() {
        uiDevice.executeShellCommand("settings put global transition_animation_scale 0")
        uiDevice.executeShellCommand("settings put global window_animation_scale 0")
        uiDevice.executeShellCommand("settings put global animator_duration_scale 0")
    }

    actual fun enable() {
        uiDevice.executeShellCommand("settings put global transition_animation_scale 1")
        uiDevice.executeShellCommand("settings put global window_animation_scale 1")
        uiDevice.executeShellCommand("settings put global animator_duration_scale 1")
    }
}

actual val platformModule: Module
    get() = module {
        single<Context> { InstrumentationRegistry.getInstrumentation().targetContext }
    }
