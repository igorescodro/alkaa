package com.escodro.task.presentation.detail.alarm

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.escodro.permission.api.PermissionController
import com.escodro.task.model.AlarmInterval
import kotlinx.datetime.LocalDateTime

/**
 * State holder for the [AlarmSelection] composable.
 */
class AlarmSelectionState(
    calendar: LocalDateTime?,
    alarmInterval: AlarmInterval?,
    permissionsController: PermissionController,
) {

    /**
     * The [PermissionController] to request the permissions on each platform.
     */
    var permissionsController by mutableStateOf(permissionsController)

    /**
     * The alarm date, if set.
     */
    var date by mutableStateOf(calendar)

    /**
     * The alarm data, if set.
     */
    var alarmInterval by mutableStateOf(alarmInterval)

    /**
     * The Exact Alarm permission dialog visibility state.
     */
    var isExactAlarmDialogOpen by mutableStateOf(false)

    /**
     * The Notification permission dialog visibility state.
     */
    var isNotificationDialogOpen by mutableStateOf(false)

    /**
     * The Notification Rationale dialog visibility state.
     */
    var isRationaleDialogOpen by mutableStateOf(false)

    /**
     * The Date and Time Picker dialog visibility state.
     */
    var isDateTimePickerDialogOpen by mutableStateOf(false)
}

@Composable
internal fun rememberAlarmSelectionState(
    calendar: LocalDateTime?,
    alarmInterval: AlarmInterval?,
    permissionsController: PermissionController,
): AlarmSelectionState =
    remember {
        AlarmSelectionState(
            calendar = calendar,
            alarmInterval = alarmInterval,
            permissionsController = permissionsController,
        )
    }
