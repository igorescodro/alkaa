package com.escodro.task.presentation.detail.alarm

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.escodro.task.model.AlarmInterval
import kotlinx.datetime.LocalDateTime

/**
 * State holder for the [AlarmSelection] composable.
 */
internal class AlarmSelectionState(calendar: LocalDateTime?, alarmInterval: AlarmInterval?) {

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
}

@Composable
internal fun rememberAlarmSelectionState(calendar: LocalDateTime?, alarmInterval: AlarmInterval?) =
    remember { AlarmSelectionState(calendar, alarmInterval) }
