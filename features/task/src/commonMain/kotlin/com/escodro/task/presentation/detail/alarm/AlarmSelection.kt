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
import com.escodro.permission.api.PermissionController
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
    permissionsController: PermissionController = koinInject(),
) {
    val alarmSelectionState =
        rememberAlarmSelectionState(
            calendar = calendar,
            alarmInterval = interval,
            permissionsController = permissionsController,
        )

    // Date Time Picker dialog
    DateTimerPicker(
        initialDateTime = alarmSelectionState.date,
        isDialogOpen = alarmSelectionState.isDateTimePickerDialogOpen,
        onCloseDialog = { alarmSelectionState.isDateTimePickerDialogOpen = false },
        onDateChange = { dateTime ->
            onAlarmUpdate(dateTime)
            alarmSelectionState.date = dateTime
        },
    )

    // Exact Alarm permission dialog
    AlarmPermissionDialog(
        isDialogOpen = alarmSelectionState.isExactAlarmDialogOpen,
        onCloseDialog = { alarmSelectionState.isExactAlarmDialogOpen = false },
        openExactAlarmPermissionScreen = openExactAlarmPermissionScreen,
    )

    // Notification permission dialog
    NotificationPermissionDialog(
        alarmSelectionState = alarmSelectionState,
        isDialogOpen = alarmSelectionState.isNotificationDialogOpen,
        onCloseDialog = { alarmSelectionState.isNotificationDialogOpen = false },
    )

    // Rationale permission dialog
    RationalePermissionDialog(
        isDialogOpen = alarmSelectionState.isRationaleDialogOpen,
        onCloseDialog = { alarmSelectionState.isRationaleDialogOpen = false },
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
            AlarmInfo(
                date = alarmSelectionState.date,
                onRemoveDate = {
                    alarmSelectionState.date = null
                    onAlarmUpdate(null)
                },
            )
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
