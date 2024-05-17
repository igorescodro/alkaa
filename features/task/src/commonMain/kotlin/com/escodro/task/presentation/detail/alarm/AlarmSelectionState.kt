package com.escodro.task.presentation.detail.alarm

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.escodro.task.model.AlarmInterval
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import kotlinx.datetime.LocalDateTime

/**
 * State holder for the [AlarmSelection] composable.
 */
class AlarmSelectionState(
    calendar: LocalDateTime?,
    alarmInterval: AlarmInterval?,
    permissionsController: PermissionsController,
) {

    /**
     * The [PermissionsController] to request the permissions on each platform.
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
    var showExactAlarmDialog by mutableStateOf(false)

    /**
     * The Notification permission dialog visibility state.
     */
    var showNotificationDialog by mutableStateOf(false)

    /**
     * The Notification Rationale dialog visibility state.
     */
    var showRationaleDialog by mutableStateOf(false)

    /**
     * The Date and Time Picker dialog visibility state.
     */
    var showDateTimePickerDialog by mutableStateOf(false)
}

@Composable
internal fun rememberAlarmSelectionState(
    calendar: LocalDateTime?,
    alarmInterval: AlarmInterval?,
): AlarmSelectionState {
    val factory = rememberPermissionsControllerFactory()
    val permissionsController = remember(factory) { factory.createPermissionsController() }
    BindEffect(permissionsController) // Required by Moko Permissions to bind in the lifecycle
    return remember {
        AlarmSelectionState(
            calendar = calendar,
            alarmInterval = alarmInterval,
            permissionsController = permissionsController,
        )
    }
}
