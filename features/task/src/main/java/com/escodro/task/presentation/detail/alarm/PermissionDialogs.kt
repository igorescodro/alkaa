package com.escodro.task.presentation.detail.alarm

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.escodro.designsystem.components.AlkaaDialog
import com.escodro.designsystem.components.DialogArguments
import com.escodro.task.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState

@Composable
internal fun AlarmPermissionDialog(
    context: Context,
    isDialogOpen: Boolean,
    onCloseDialog: () -> Unit,
) {
    val arguments = DialogArguments(
        title = stringResource(id = R.string.task_alarm_permission_dialog_title),
        text = stringResource(id = R.string.task_alarm_permission_dialog_text),
        confirmText = stringResource(id = R.string.task_alarm_permission_dialog_confirm),
        dismissText = stringResource(id = R.string.task_alarm_permission_dialog_cancel),
        onConfirmAction = {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val intent = Intent().apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
                    data = Uri.fromParts("package", context.packageName, null)
                }
                context.startActivity(intent)
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

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun NotificationPermissionDialog(
    permissionState: PermissionState,
    isDialogOpen: Boolean,
    onCloseDialog: () -> Unit,
) {
    val arguments = DialogArguments(
        title = stringResource(id = R.string.task_notification_permission_dialog_title),
        text = stringResource(id = R.string.task_notification_permission_dialog_text),
        confirmText = stringResource(id = R.string.task_notification_permission_dialog_confirm),
        dismissText = stringResource(id = R.string.task_notification_permission_dialog_cancel),
        onConfirmAction = {
            permissionState.launchPermissionRequest()
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
internal fun RationalePermissionDialog(
    context: Context,
    isDialogOpen: Boolean,
    onCloseDialog: () -> Unit,
) {
    val arguments = DialogArguments(
        title = stringResource(id = R.string.task_notification_rationale_dialog_title),
        text = stringResource(id = R.string.task_notification_rationale_dialog_text),
        confirmText = stringResource(id = R.string.task_notification_rationale_dialog_confirm),
        dismissText = stringResource(id = R.string.task_notification_rationale_dialog_cancel),
        onConfirmAction = {
            val intent = Intent().apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                data = Uri.fromParts("package", context.packageName, null)
            }
            context.startActivity(intent)
            onCloseDialog()
        },
    )
    AlkaaDialog(
        arguments = arguments,
        isDialogOpen = isDialogOpen,
        onDismissRequest = onCloseDialog,
    )
}
