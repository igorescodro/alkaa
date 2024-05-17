package com.escodro.task.presentation.detail.alarm.interactor

import com.escodro.task.presentation.detail.alarm.AlarmSelectionState
import dev.icerock.moko.permissions.Permission
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

internal class OpenAlarmSchedulerImpl : OpenAlarmScheduler {
    override fun invoke(
        coroutineScope: CoroutineScope,
        alarmSelectionState: AlarmSelectionState,
        hasExactAlarmPermission: () -> Boolean,
    ) {
        coroutineScope.launch {
            val isNotificationPermissionGranted = alarmSelectionState.permissionsController
                .isPermissionGranted(Permission.REMOTE_NOTIFICATION)

            if (hasExactAlarmPermission() && isNotificationPermissionGranted) {
                alarmSelectionState.showDateTimePickerDialog = true
            } else {
                alarmSelectionState.showExactAlarmDialog = !hasExactAlarmPermission()
                alarmSelectionState.showNotificationDialog = !isNotificationPermissionGranted
            }
        }
    }
}
