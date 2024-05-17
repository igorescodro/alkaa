@file:Suppress("ktlint:standard:filename")

package com.escodro.alkaa.test

import platform.UIKit.UIView

actual class PlatformAnimation actual constructor() {
    actual fun disable() {
        UIView.setAnimationsEnabled(false)
    }

    actual fun enable() {
        UIView.setAnimationsEnabled(true)
    }
}
