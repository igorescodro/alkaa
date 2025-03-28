package com.escodro.permission.api

internal class DesktopPermissionController : PermissionController {

    override val controller: Any = Any()

    override suspend fun requestPermission(permission: Permission) {
        // TODO: Implement requestPermission
    }

    override suspend fun isPermissionGranted(permission: Permission): Boolean {
        // TODO: Implement isPermissionGranted
        return false
    }
}
