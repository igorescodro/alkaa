package com.escodro.task.presentation.detail.alarm

import androidx.compose.runtime.Composable
import com.escodro.task.model.AlarmInterval
import kotlinx.datetime.LocalDateTime

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
@Suppress("LongParameterList", "NewApi")
@Composable
internal fun AlarmSelection(
    calendar: LocalDateTime?,
    interval: AlarmInterval?,
    onAlarmUpdate: (LocalDateTime?) -> Unit,
    onIntervalSelect: (AlarmInterval) -> Unit,
    hasAlarmPermission: () -> Boolean,
    shouldCheckNotificationPermission: Boolean,
) {
    // val context = LocalContext.current
    // val permissionState = if (shouldCheckNotificationPermission) {
    //     rememberPermissionState(permission = POST_NOTIFICATIONS)
    // } else {
    //     PermissionStateFactory.getGrantedPermissionState(permission = POST_NOTIFICATIONS)
    // }
    // val state = rememberAlarmSelectionState(calendar = calendar, alarmInterval = interval)
    //
    // // Exact Alarm permission dialog
    // AlarmPermissionDialog(
    //     context = context,
    //     isDialogOpen = state.showExactAlarmDialog,
    //     onCloseDialog = { state.showExactAlarmDialog = false },
    // )
    //
    // // Notification permission dialog
    // NotificationPermissionDialog(
    //     permissionState = permissionState,
    //     isDialogOpen = state.showNotificationDialog,
    //     onCloseDialog = { state.showNotificationDialog = false },
    // )
    //
    // // Rationale permission dialog
    // RationalePermissionDialog(
    //     context = context,
    //     isDialogOpen = state.showRationaleDialog,
    //     onCloseDialog = { state.showRationaleDialog = false },
    // )
    //
    // AlarmSelectionContent(
    //     context = context,
    //     state = state,
    //     permissionState = permissionState,
    //     hasAlarmPermission = hasAlarmPermission,
    //     onAlarmUpdate = onAlarmUpdate,
    //     onIntervalSelect = onIntervalSelect,
    // )
}

// @Suppress("LongParameterList")
// @Composable
// internal fun AlarmSelectionContent(
//     context: Context,
//     state: AlarmSelectionState,
//     permissionState: PermissionState,
//     hasAlarmPermission: () -> Boolean,
//     onAlarmUpdate: (Calendar?) -> Unit,
//     onIntervalSelect: (AlarmInterval) -> Unit,
// ) {
//     Column {
//         TaskDetailSectionContent(
//             modifier = Modifier
//                 .height(56.dp)
//                 .clickable {
//                     when {
//                         hasAlarmPermission() && permissionState.status.isGranted ->
//                             DateTimePickerDialog(context) { calendar ->
//                                 state.date = calendar
//                                 onAlarmUpdate(calendar)
//                             }.show()
//                         permissionState.status.shouldShowRationale ->
//                             state.showRationaleDialog = true
//                         else -> {
//                             state.showExactAlarmDialog = !hasAlarmPermission()
//                             state.showNotificationDialog = !permissionState.status.isGranted
//                         }
//                     }
//                 },
//             imageVector = Icons.Outlined.Alarm,
//             contentDescription = R.string.task_detail_cd_icon_alarm,
//         ) {
//             AlarmInfo(state.date) {
//                 state.date = null
//                 onAlarmUpdate(null)
//             }
//         }
//         AlarmIntervalSelection(
//             date = state.date,
//             alarmInterval = state.alarmInterval,
//             onIntervalSelect = { interval ->
//                 state.alarmInterval = interval
//                 onIntervalSelect(interval)
//             },
//         )
//     }
// }
