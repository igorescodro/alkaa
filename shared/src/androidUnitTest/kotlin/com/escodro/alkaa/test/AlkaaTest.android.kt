@file:Suppress("ktlint:standard:filename")

package com.escodro.alkaa.test

import org.koin.core.module.Module
import org.koin.dsl.module

actual class PlatformAnimation actual constructor() {
    actual fun disable() {
        // Do nothing
    }

    actual fun enable() {
        // Do nothing
    }
}

actual val platformModule: Module
    get() = module { }
