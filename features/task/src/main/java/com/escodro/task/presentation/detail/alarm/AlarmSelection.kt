package com.escodro.task.presentation.detail.alarm

import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.escodro.core.view.DateTimePickerDialog
import com.escodro.designsystem.AlkaaTheme
import com.escodro.task.R
import com.escodro.task.model.AlarmInterval
import com.escodro.task.permission.PermissionStateFactory
import com.escodro.task.presentation.detail.TaskDetailSectionContent
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import java.util.Calendar

/**
 * Render the Alarm Section, including the alarm set and the alarm interval.
 *
 * @param calendar the alarm set, `null` if no alarm
 * @param interval the alarm repeat interval, `null` if no repeat interval
 * @param onAlarmUpdate lambda called when the alarm updates
 * @param onIntervalSelect lambda called when the interval updates
 * @param hasAlarmPermission lambda to check if Exact Alarm permission is granted
 * @param shouldCheckNotificationPermission `true` if the notification permission should be ask
 */
@Suppress("LongParameterList")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun AlarmSelection(
    calendar: Calendar?,
    interval: AlarmInterval?,
    onAlarmUpdate: (Calendar?) -> Unit,
    onIntervalSelect: (AlarmInterval) -> Unit,
    hasAlarmPermission: () -> Boolean,
    shouldCheckNotificationPermission: Boolean,
) {
    val context = LocalContext.current
    val permissionState = if (shouldCheckNotificationPermission) {
        rememberPermissionState(permission = POST_NOTIFICATIONS)
    } else {
        PermissionStateFactory.getGrantedPermissionState(permission = POST_NOTIFICATIONS)
    }
    val state = rememberAlarmSelectionState(calendar = calendar, alarmInterval = interval)

    // Exact Alarm permission dialog
    AlarmPermissionDialog(
        context = context,
        isDialogOpen = state.showExactAlarmDialog,
        onCloseDialog = { state.showExactAlarmDialog = false },
    )

    // Notification permission dialog
    NotificationPermissionDialog(
        permissionState = permissionState,
        isDialogOpen = state.showNotificationDialog,
        onCloseDialog = { state.showNotificationDialog = false },
    )

    // Rationale permission dialog
    RationalePermissionDialog(
        context = context,
        isDialogOpen = state.showRationaleDialog,
        onCloseDialog = { state.showRationaleDialog = false },
    )

    AlarmSelectionContent(
        context = context,
        state = state,
        permissionState = permissionState,
        hasAlarmPermission = hasAlarmPermission,
        onAlarmUpdate = onAlarmUpdate,
        onIntervalSelect = onIntervalSelect,
    )
}

@Suppress("LongParameterList")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun AlarmSelectionContent(
    context: Context,
    state: AlarmSelectionState,
    permissionState: PermissionState,
    hasAlarmPermission: () -> Boolean,
    onAlarmUpdate: (Calendar?) -> Unit,
    onIntervalSelect: (AlarmInterval) -> Unit,
) {
    Column {
        TaskDetailSectionContent(
            modifier = Modifier
                .height(56.dp)
                .clickable {
                    when {
                        hasAlarmPermission() && permissionState.status.isGranted ->
                            DateTimePickerDialog(context) { calendar ->
                                state.date = calendar
                                onAlarmUpdate(calendar)
                            }.show()
                        permissionState.status.shouldShowRationale ->
                            state.showRationaleDialog = true
                        else -> {
                            state.showExactAlarmDialog = !hasAlarmPermission()
                            state.showNotificationDialog = !permissionState.status.isGranted
                        }
                    }
                },
            imageVector = Icons.Outlined.Alarm,
            contentDescription = R.string.task_detail_cd_icon_alarm,
        ) {
            AlarmInfo(state.date) {
                state.date = null
                onAlarmUpdate(null)
            }
        }
        AlarmIntervalSelection(
            date = state.date,
            alarmInterval = state.alarmInterval,
            onIntervalSelect = { interval ->
                state.alarmInterval = interval
                onIntervalSelect(interval)
            },
        )
    }
}

@Suppress("UndocumentedPublicFunction")
@Preview
@Composable
fun AlarmSetSelectionPreview() {
    Surface(color = MaterialTheme.colorScheme.background) {
        AlkaaTheme {
            AlarmSelection(
                Calendar.getInstance(),
                AlarmInterval.WEEKLY,
                onAlarmUpdate = {},
                onIntervalSelect = {},
                hasAlarmPermission = { true },
                shouldCheckNotificationPermission = true,
            )
        }
    }
}

@Suppress("UndocumentedPublicFunction")
@Preview
@Composable
fun AlarmNotSetSelectionPreview() {
    Surface(color = MaterialTheme.colorScheme.background) {
        AlkaaTheme {
            AlarmSelection(
                null,
                AlarmInterval.NEVER,
                onAlarmUpdate = {},
                onIntervalSelect = {},
                hasAlarmPermission = { true },
                shouldCheckNotificationPermission = true,
            )
        }
    }
}
