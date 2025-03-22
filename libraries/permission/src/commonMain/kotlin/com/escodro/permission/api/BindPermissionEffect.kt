package com.escodro.permission.api

import androidx.compose.runtime.Composable

/**
 * Binds the permission controller to the LocalLifecycleOwner in the required platform.
 *
 * @param permissionController the permission controller to be bound
 */
@Composable
expect fun BindPermissionEffect(permissionController: PermissionController)
