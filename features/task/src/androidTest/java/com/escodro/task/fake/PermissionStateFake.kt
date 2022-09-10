package com.escodro.task.fake

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus

@OptIn(ExperimentalPermissionsApi::class)
internal class PermissionStateFake(
    private val fakeStatus: PermissionStatus
) : PermissionState {

    override val permission: String
        get() = "awesome permission for naughty stuff"

    override val status: PermissionStatus
        get() = fakeStatus

    override fun launchPermissionRequest() {
        // Do nothing
    }
}
