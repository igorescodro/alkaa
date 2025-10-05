package com.escodro.task.fake

import androidx.activity.ComponentActivity
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.PermissionsController

internal actual class PermissionControllerFake : PermissionsController {

    actual var isPermissionGrantedValue: Boolean = false
    actual var permissionStateValue: PermissionState = PermissionState.NotDetermined
    actual var throwsException: Boolean = false

    override suspend fun getPermissionState(permission: Permission): PermissionState =
        permissionStateValue

    override suspend fun isPermissionGranted(permission: Permission): Boolean =
        isPermissionGrantedValue

    override fun openAppSettings() {
        TODO("Not yet implemented")
    }

    override fun bind(activity: ComponentActivity) {
        // Do nothing
    }

    override suspend fun providePermission(permission: Permission) {
        if (throwsException) {
            throw Exception("Permission not granted")
        }
    }

    actual fun clean() {
        isPermissionGrantedValue = false
        permissionStateValue = PermissionState.NotDetermined
        throwsException = false
    }
}
