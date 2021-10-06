package com.escodro.domain.usecase.alarm

import com.escodro.domain.interactor.AlarmPermissionInteractor

/**
 * Use case to show an information and request the user to grant the Alarm permission.
 */
class ShowAlarmPermissionInfo(private val permissionInteractor: AlarmPermissionInteractor) {

    /**
     * Shows an information and requests the user to grant the Alarm permission.
     */
    operator fun invoke() {
        permissionInteractor.requestPermission()
    }
}
