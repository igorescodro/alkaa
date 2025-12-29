package com.escodro.task.presentation.detail.alarm

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.escodro.designsystem.components.dialog.AlkaaDialog
import com.escodro.designsystem.components.dialog.DialogArguments
import com.escodro.permission.api.Permission
import com.escodro.resources.Res
import com.escodro.resources.task_alarm_permission_dialog_cancel
import com.escodro.resources.task_alarm_permission_dialog_confirm
import com.escodro.resources.task_alarm_permission_dialog_text
import com.escodro.resources.task_alarm_permission_dialog_title
import com.escodro.resources.task_notification_permission_dialog_cancel
import com.escodro.resources.task_notification_permission_dialog_confirm
import com.escodro.resources.task_notification_permission_dialog_text
import com.escodro.resources.task_notification_permission_dialog_title
import com.escodro.resources.task_notification_rationale_dialog_cancel
import com.escodro.resources.task_notification_rationale_dialog_confirm
import com.escodro.resources.task_notification_rationale_dialog_text
import com.escodro.resources.task_notification_rationale_dialog_title
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import mu.KotlinLogging
import org.jetbrains.compose.resources.stringResource

private val logger = KotlinLogging.logger {}

@Composable
internal fun AlarmPermissionDialog(
    isDialogOpen: Boolean,
    onCloseDialog: () -> Unit,
    openExactAlarmPermissionScreen: () -> Unit,
) {
    val arguments = DialogArguments(
        title = stringResource(Res.string.task_alarm_permission_dialog_title),
        text = stringResource(Res.string.task_alarm_permission_dialog_text),
        confirmText = stringResource(Res.string.task_alarm_permission_dialog_confirm),
        dismissText = stringResource(Res.string.task_alarm_permission_dialog_cancel),
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

@Suppress("TooGenericExceptionCaught")
@Composable
internal fun NotificationPermissionDialog(
    alarmSelectionState: AlarmSelectionState,
    isDialogOpen: Boolean,
    onCloseDialog: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val arguments = DialogArguments(
        title = stringResource(Res.string.task_notification_permission_dialog_title),
        text = stringResource(Res.string.task_notification_permission_dialog_text),
        confirmText = stringResource(Res.string.task_notification_permission_dialog_confirm),
        dismissText = stringResource(Res.string.task_notification_permission_dialog_cancel),
        onConfirmAction = {
            scope.launch {
                try {
                    alarmSelectionState.permissionsController
                        .requestPermission(Permission.NOTIFICATION)
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    alarmSelectionState.isRationaleDialogOpen = true
                    logger.error(e) { "Error while requesting Notification permission." }
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
        title = stringResource(Res.string.task_notification_rationale_dialog_title),
        text = stringResource(Res.string.task_notification_rationale_dialog_text),
        confirmText = stringResource(Res.string.task_notification_rationale_dialog_confirm),
        dismissText = stringResource(Res.string.task_notification_rationale_dialog_cancel),
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
