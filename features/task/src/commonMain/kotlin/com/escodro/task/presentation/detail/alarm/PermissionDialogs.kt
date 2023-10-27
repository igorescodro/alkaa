package com.escodro.task.presentation.detail.alarm

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.escodro.designsystem.components.AlkaaDialog
import com.escodro.designsystem.components.DialogArguments
import com.escodro.resources.MR
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.launch

@Composable
internal fun AlarmPermissionDialog(
    isDialogOpen: Boolean,
    onCloseDialog: () -> Unit,
    openExactAlarmPermissionScreen: () -> Unit,
) {
    val arguments = DialogArguments(
        title = stringResource(MR.strings.task_alarm_permission_dialog_title),
        text = stringResource(MR.strings.task_alarm_permission_dialog_text),
        confirmText = stringResource(MR.strings.task_alarm_permission_dialog_confirm),
        dismissText = stringResource(MR.strings.task_alarm_permission_dialog_cancel),
        onConfirmAction = {
            openExactAlarmPermissionScreen()
            onCloseDialog()
        },
    )
    AlkaaDialog(
        arguments = arguments,
        isDialogOpen = isDialogOpen,
        onDismissRequest = onCloseDialog,
    )
}

@Composable
internal fun NotificationPermissionDialog(
    alarmSelectionState: AlarmSelectionState,
    isDialogOpen: Boolean,
    onCloseDialog: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val arguments = DialogArguments(
        title = stringResource(MR.strings.task_notification_permission_dialog_title),
        text = stringResource(MR.strings.task_notification_permission_dialog_text),
        confirmText = stringResource(MR.strings.task_notification_permission_dialog_confirm),
        dismissText = stringResource(MR.strings.task_notification_permission_dialog_cancel),
        onConfirmAction = {
            scope.launch {
                try {
                    alarmSelectionState.permissionsController
                        .providePermission(Permission.REMOTE_NOTIFICATION)
                } catch (deniedAlways: DeniedAlwaysException) {
                    alarmSelectionState.showRationaleDialog = true
                } catch (denied: DeniedException) {
                    alarmSelectionState.showRationaleDialog = true
                }

                onCloseDialog()
            }
        },
    )
    AlkaaDialog(
        arguments = arguments,
        isDialogOpen = isDialogOpen,
        onDismissRequest = onCloseDialog,
    )
}

@Composable
internal fun RationalePermissionDialog(
    isDialogOpen: Boolean,
    onCloseDialog: () -> Unit,
    openAppSettingsScreen: () -> Unit,
) {
    val arguments = DialogArguments(
        title = stringResource(MR.strings.task_notification_rationale_dialog_title),
        text = stringResource(MR.strings.task_notification_rationale_dialog_text),
        confirmText = stringResource(MR.strings.task_notification_rationale_dialog_confirm),
        dismissText = stringResource(MR.strings.task_notification_rationale_dialog_cancel),
        onConfirmAction = {
            openAppSettingsScreen()
            onCloseDialog()
        },
    )
    AlkaaDialog(
        arguments = arguments,
        isDialogOpen = isDialogOpen,
        onDismissRequest = onCloseDialog,
    )
}
