@file:Suppress("ktlint:standard:filename")

package com.escodro.desktopapp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.escodro.resources.Res
import com.escodro.resources.content_app_name
import com.escodro.shared.AlkaaMultiplatformApp
import com.escodro.shared.di.initKoin
import org.jetbrains.compose.resources.stringResource

fun main() = application {
    initKoin()

    Window(
        onCloseRequest = ::exitApplication,
        title = stringResource(Res.string.content_app_name),
    ) {
        AlkaaMultiplatformApp()
    }
}
