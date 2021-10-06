package com.escodro.alarm.interactor

import com.escodro.alarm.notification.PermissionNotification
import com.escodro.domain.interactor.AlarmPermissionInteractor

internal class AlarmPermissionInteractorImpl(
    private val permissionNotification: PermissionNotification
) : AlarmPermissionInteractor {

    override fun requestPermission() {
        permissionNotification.show()
    }
}
