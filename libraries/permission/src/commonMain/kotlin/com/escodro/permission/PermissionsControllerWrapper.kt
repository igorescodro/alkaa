package com.escodro.permission

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory

/**
 * Wrapper to provide the [PermissionsController] instance. This class holds a single instance of
 * [PermissionsController] since Moko does not allow `BindEffect` to be called outside an Activity
 * context (Alkaa uses in a `dialog` destination).
 */
class PermissionsControllerWrapper {

    private var instance: PermissionsController? = null

    @Composable
    fun getInstance(): PermissionsController {
        if (instance == null) {
            val factory = rememberPermissionsControllerFactory()
            val permissionsController = remember(factory) { factory.createPermissionsController() }
            instance = permissionsController
        }
        return instance!!
    }
}
