package com.escodro.permission.api

@Suppress("ForbiddenComment")
internal class DesktopPermissionController : PermissionController {

    override val controller: Any = Any()

    override suspend fun requestPermission(permission: Permission) {
        // TODO: Implement requestPermission
    }

    @Suppress("ExpressionBodySyntax")
    override suspend fun isPermissionGranted(permission: Permission): Boolean {
        // TODO: Implement isPermissionGranted
        return false
    }
}
