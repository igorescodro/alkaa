package com.escodro.task.presentation.detail.alarm

import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Repeat
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.escodro.core.extension.format
import com.escodro.core.view.DateTimePickerDialog
import com.escodro.designsystem.AlkaaTheme
import com.escodro.task.R
import com.escodro.task.model.AlarmInterval
import com.escodro.task.presentation.detail.TaskDetailSectionContent
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import java.util.Calendar

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun AlarmSelection(
    calendar: Calendar?,
    interval: AlarmInterval?,
    onAlarmUpdate: (Calendar?) -> Unit,
    onIntervalSelect: (AlarmInterval) -> Unit,
    hasAlarmPermission: () -> Boolean
) {
    val context = LocalContext.current
    val permissionState = rememberPermissionState(permission = POST_NOTIFICATIONS)
    val state = rememberAlarmSelectionState(calendar = calendar, alarmInterval = interval)

    // Exact Alarm permission dialog
    AlarmPermissionDialog(
        context = context,
        isDialogOpen = state.showExactAlarmDialog,
        onCloseDialog = { state.showExactAlarmDialog = false }
    )

    // Notification permission dialog
    NotificationPermissionDialog(
        permissionState = permissionState,
        isDialogOpen = state.showNotificationDialog,
        onCloseDialog = { state.showNotificationDialog = false }
    )

    // Rationale permission dialog
    RationalePermissionDialog(
        context = context,
        isDialogOpen = state.showRationaleDialog,
        onCloseDialog = { state.showRationaleDialog = false }
    )

    AlarmSelectionContent(
        context = context,
        state = state,
        permissionState = permissionState,
        hasAlarmPermission = hasAlarmPermission,
        onAlarmUpdate = onAlarmUpdate,
        onIntervalSelect = onIntervalSelect
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun AlarmSelectionContent(
    context: Context,
    state: AlarmSelectionState,
    permissionState: PermissionState,
    hasAlarmPermission: () -> Boolean,
    onAlarmUpdate: (Calendar?) -> Unit,
    onIntervalSelect: (AlarmInterval) -> Unit
) {
    Column {
        TaskDetailSectionContent(
            modifier = Modifier
                .height(56.dp)
                .clickable {
                    when {
                        hasAlarmPermission() && permissionState.status.isGranted -> {
                            DateTimePickerDialog(context) { calendar ->
                                state.date = calendar
                                onAlarmUpdate(calendar)
                            }.show()
                        }
                        permissionState.status.shouldShowRationale -> {
                            state.showRationaleDialog = true
                        }
                        else -> {
                            state.showExactAlarmDialog = !hasAlarmPermission()
                            state.showNotificationDialog = !permissionState.status.isGranted
                        }
                    }
                },
            imageVector = Icons.Outlined.Alarm,
            contentDescription = R.string.task_detail_cd_icon_alarm
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
            }
        )
    }
}

@Composable
private fun AlarmInfo(
    date: Calendar?,
    onRemoveDate: () -> Unit
) {
    Column {
        if (date == null) {
            NoAlarmSet()
        } else {
            AlarmSet(
                date = null,
                onRemoveClick = onRemoveDate
            )
        }
    }
}

@Composable
private fun AlarmIntervalSelection(
    date: Calendar?,
    alarmInterval: AlarmInterval?,
    onIntervalSelect: (AlarmInterval) -> Unit
) {
    val showDialog = remember { mutableStateOf(false) }
    if (date != null) {
        AlarmIntervalDialog(showDialog) { interval -> onIntervalSelect(interval) }

        TaskDetailSectionContent(
            modifier = Modifier
                .height(56.dp)
                .clickable { showDialog.value = true },
            imageVector = Icons.Outlined.Repeat,
            contentDescription = R.string.task_detail_cd_icon_repeat_alarm
        ) {
            val index = alarmInterval?.index ?: 0
            Text(
                text = stringArrayResource(id = R.array.task_alarm_repeating)[index],
                color = MaterialTheme.colors.onSecondary
            )
        }
    }
}

@Composable
private fun AlarmSet(date: Calendar?, onRemoveClick: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Text(
            text = date?.format() ?: "",
            color = MaterialTheme.colors.onSecondary
        )
        IconButton(onClick = onRemoveClick) {
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = stringResource(
                    id = R.string.task_detail_cd_icon_remove_alarm
                )
            )
        }
    }
}

@Composable
private fun NoAlarmSet() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = stringResource(id = R.string.task_detail_alarm_no_alarm),
            color = MaterialTheme.colors.onSecondary
        )
    }
}

@Composable
private fun AlarmIntervalDialog(
    showDialog: MutableState<Boolean>,
    onIntervalSelect: (AlarmInterval) -> Unit
) {
    if (showDialog.value.not()) {
        return
    }

    Dialog(onDismissRequest = { showDialog.value = false }) {
        Surface(
            color = MaterialTheme.colors.background,
            modifier = Modifier.fillMaxWidth()
        ) {
            val intervalList = stringArrayResource(id = R.array.task_alarm_repeating)
            LazyColumn(modifier = Modifier.padding(24.dp)) {
                itemsIndexed(
                    items = intervalList,
                    itemContent = { index, title ->
                        AlarmListItem(
                            title = title,
                            index = index,
                            showDialog = showDialog,
                            onIntervalSelect = onIntervalSelect
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun AlarmListItem(
    title: String,
    index: Int,
    showDialog: MutableState<Boolean>,
    onIntervalSelect: (AlarmInterval) -> Unit
) {
    Text(
        text = title,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable {
                val interval =
                    AlarmInterval.values().find { it.index == index } ?: AlarmInterval.NEVER
                onIntervalSelect(interval)
                showDialog.value = false
            }
    )
}

@Suppress("UndocumentedPublicFunction")
@Preview
@Composable
fun AlarmSetSelectionPreview() {
    Surface(color = MaterialTheme.colors.background) {
        AlkaaTheme {
            AlarmSelection(
                Calendar.getInstance(),
                AlarmInterval.WEEKLY,
                onAlarmUpdate = {},
                onIntervalSelect = {},
                hasAlarmPermission = { true }
            )
        }
    }
}

@Suppress("UndocumentedPublicFunction")
@Preview
@Composable
fun AlarmNotSetSelectionPreview() {
    Surface(color = MaterialTheme.colors.background) {
        AlkaaTheme {
            AlarmSelection(
                null,
                AlarmInterval.NEVER,
                onAlarmUpdate = {},
                onIntervalSelect = {},
                hasAlarmPermission = { true }
            )
        }
    }
}

@Suppress("UndocumentedPublicFunction")
@Preview
@Composable
fun AlarmIntervalDialogPreview() {
    AlkaaTheme {
        AlarmIntervalDialog(
            showDialog = remember { mutableStateOf(true) },
            onIntervalSelect = {}
        )
    }
}
