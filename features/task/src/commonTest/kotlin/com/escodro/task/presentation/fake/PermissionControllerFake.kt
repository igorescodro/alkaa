package com.escodro.task.presentation.fake

import com.escodro.permission.api.Permission
import com.escodro.permission.api.PermissionController

class PermissionControllerFake : PermissionController {
    override val controller: Any = Unit

    var isPermissionGrantedValue: Boolean = true

    var isExceptionThrown: Boolean = false

    @Suppress("TooGenericExceptionThrown")
    override suspend fun requestPermission(permission: Permission) {
        if (isExceptionThrown) {
            throw Exception("Permission denied")
        }
    }

    override suspend fun isPermissionGranted(permission: Permission): Boolean =
        isPermissionGrantedValue

    fun clean() {
        isPermissionGrantedValue = true
        isExceptionThrown = false
    }
}
