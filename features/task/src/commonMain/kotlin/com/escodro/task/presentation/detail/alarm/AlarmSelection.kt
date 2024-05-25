package com.escodro.task.presentation.detail.alarm

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.escodro.resources.Res
import com.escodro.resources.task_detail_cd_icon_alarm
import com.escodro.task.model.AlarmInterval
import com.escodro.task.presentation.detail.TaskDetailSectionContent
import com.escodro.task.presentation.detail.alarm.interactor.OpenAlarmScheduler
import kotlinx.datetime.LocalDateTime
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

/**
 * Render the Alarm Section, including the alarm set and the alarm interval.
 *
 * @param calendar the alarm set, `null` if no alarm
 * @param interval the alarm repeat interval, `null` if no repeat interval
 * @param onAlarmUpdate lambda called when the alarm updates
 * @param onIntervalSelect lambda called when the interval updates
 * @param hasExactAlarmPermission lambda to check if Exact Alarm permission is granted
 * @param openExactAlarmPermissionScreen lambda to open the Exact Alarm permission screen
 * @param openAppSettingsScreen lambda to open the Permissions screen
 */
@Suppress("LongParameterList", "NewApi")
@Composable
internal fun AlarmSelection(
    calendar: LocalDateTime?,
    interval: AlarmInterval?,
    onAlarmUpdate: (LocalDateTime?) -> Unit,
    onIntervalSelect: (AlarmInterval) -> Unit,
    hasExactAlarmPermission: () -> Boolean,
    openExactAlarmPermissionScreen: () -> Unit,
    openAppSettingsScreen: () -> Unit,
) {
    val alarmSelectionState =
        rememberAlarmSelectionState(calendar = calendar, alarmInterval = interval)

    // Date Time Picker dialog
    DateTimerPicker(
        isDialogOpen = alarmSelectionState.showDateTimePickerDialog,
        onCloseDialog = { alarmSelectionState.showDateTimePickerDialog = false },
        onDateChanged = { dateTime ->
            onAlarmUpdate(dateTime)
            alarmSelectionState.date = dateTime
        },
    )

    // Exact Alarm permission dialog
    AlarmPermissionDialog(
        isDialogOpen = alarmSelectionState.showExactAlarmDialog,
        onCloseDialog = { alarmSelectionState.showExactAlarmDialog = false },
        openExactAlarmPermissionScreen = openExactAlarmPermissionScreen,
    )

    // Notification permission dialog
    NotificationPermissionDialog(
        alarmSelectionState = alarmSelectionState,
        isDialogOpen = alarmSelectionState.showNotificationDialog,
        onCloseDialog = { alarmSelectionState.showNotificationDialog = false },
    )

    // Rationale permission dialog
    RationalePermissionDialog(
        isDialogOpen = alarmSelectionState.showRationaleDialog,
        onCloseDialog = { alarmSelectionState.showRationaleDialog = false },
        openAppSettingsScreen = openAppSettingsScreen,
    )

    AlarmSelectionContent(
        alarmSelectionState = alarmSelectionState,
        hasExactAlarmPermission = hasExactAlarmPermission,
        onAlarmUpdate = onAlarmUpdate,
        onIntervalSelect = onIntervalSelect,
    )
}

@Suppress("LongParameterList")
@Composable
internal fun AlarmSelectionContent(
    alarmSelectionState: AlarmSelectionState,
    hasExactAlarmPermission: () -> Boolean,
    onAlarmUpdate: (LocalDateTime?) -> Unit,
    onIntervalSelect: (AlarmInterval) -> Unit,
    openAlarmScheduler: OpenAlarmScheduler = koinInject(), // iOS instrumented tests
) {
    val coroutineScope = rememberCoroutineScope()
    Column {
        TaskDetailSectionContent(
            modifier = Modifier
                .height(56.dp)
                .clickable {
                    openAlarmScheduler(
                        coroutineScope = coroutineScope,
                        alarmSelectionState = alarmSelectionState,
                        hasExactAlarmPermission = hasExactAlarmPermission,
                    )
                },
            imageVector = Icons.Outlined.Alarm,
            contentDescription = stringResource(Res.string.task_detail_cd_icon_alarm),
        ) {
            AlarmInfo(alarmSelectionState.date) {
                alarmSelectionState.date = null
                onAlarmUpdate(null)
            }
        }
        AlarmIntervalSelection(
            date = alarmSelectionState.date,
            alarmInterval = alarmSelectionState.alarmInterval,
            onIntervalSelect = { interval ->
                alarmSelectionState.alarmInterval = interval
                onIntervalSelect(interval)
            },
        )
    }
}
