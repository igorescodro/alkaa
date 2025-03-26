package com.escodro.permission.api

import androidx.compose.runtime.Composable
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.compose.BindEffect

@Composable
actual fun BindPermissionEffect(
    permissionController: PermissionController,
) {
    BindEffect(permissionController.controller as PermissionsController)
}
