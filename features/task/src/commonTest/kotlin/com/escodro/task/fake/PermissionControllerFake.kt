package com.escodro.task.fake

import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.PermissionsController

internal expect class PermissionControllerFake() : PermissionsController {

    var isPermissionGrantedValue: Boolean

    var permissionStateValue: PermissionState

    var throwsException: Boolean
    fun clean()
}
