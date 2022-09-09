package com.escodro.task.permission

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus

/**
 * Creates [PermissionState] instances based on given parameters.
 */
@OptIn(ExperimentalPermissionsApi::class)
internal object PermissionStateFactory {

    /**
     * Get a [PermissionState] with the [PermissionStatus] set as [PermissionStatus.Granted] to be
     * used in the scenarios where the permission are not available for the given Android API and
     * should be considered as already granted by the user.
     */
    fun getGrantedPermissionState(permission: String): PermissionState = object : PermissionState {
        override val permission: String
            get() = permission

        override val status: PermissionStatus
            get() = PermissionStatus.Granted

        override fun launchPermissionRequest() {
            // Do nothing
        }
    }
}
