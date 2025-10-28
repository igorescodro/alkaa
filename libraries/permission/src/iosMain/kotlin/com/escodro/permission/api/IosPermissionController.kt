package com.escodro.permission.api

import dev.icerock.moko.permissions.ios.PermissionsController
import dev.icerock.moko.permissions.notifications.REMOTE_NOTIFICATION
import dev.icerock.moko.permissions.Permission as LibPermission

internal class IosPermissionController : PermissionController {

    override val controller: PermissionsController = PermissionsController()

    override suspend fun requestPermission(permission: Permission) {
        val libPermission = mapPermission(permission)
        controller.providePermission(libPermission)
    }

    override suspend fun isPermissionGranted(permission: Permission): Boolean {
        val libPermission = mapPermission(permission)
        return controller.isPermissionGranted(libPermission)
    }

    private fun mapPermission(permission: Permission): LibPermission = when (permission) {
        Permission.NOTIFICATION -> LibPermission.REMOTE_NOTIFICATION
    }
}
