package com.escodro.permission.api

import android.content.Context
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.notifications.REMOTE_NOTIFICATION
import dev.icerock.moko.permissions.Permission as LibPermission

internal class AndroidPermissionController(
    context: Context,
) : PermissionController {

    override val controller: PermissionsController = PermissionsController(context)

    override suspend fun requestPermission(permission: Permission) {
        val libPermission = mapPermission(permission)
        controller.providePermission(libPermission)
    }

    override suspend fun isPermissionGranted(permission: Permission): Boolean {
        val libPermission = mapPermission(permission)
        return controller.isPermissionGranted(libPermission)
    }

    private fun mapPermission(permission: Permission): LibPermission =
        when (permission) {
            Permission.NOTIFICATION -> LibPermission.REMOTE_NOTIFICATION
            else -> throw IllegalArgumentException("Permission not supported")
        }
}
