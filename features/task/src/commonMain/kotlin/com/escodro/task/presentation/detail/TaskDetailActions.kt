package com.escodro.task.presentation.detail

import com.escodro.task.model.AlarmInterval
import com.escodro.task.presentation.detail.main.CategoryId
import kotlinx.datetime.LocalDateTime

/**
 * Actions to be performed by the [com.escodro.task.presentation.detail.main.TaskDetailScreen].
 *
 * @param onTitleChange lambda to be called when the title changes
 * @param onDescriptionChange lambda to be called when the description changes
 * @param onCategoryChange lambda to be called when the category changes
 * @param onAlarmChange lambda to be called when the alarm changes
 * @param onIntervalChange lambda to be called when the interval updates
 * @param hasExactAlarmPermission lambda to check if Exact Alarm permission is granted
 * @param openExactAlarmPermissionScreen lambda to open the Exact Alarm permission screen
 * @param openAppSettingsScreen lambda to open the Permissions screen
 * @param onUpPress lambda to be called when the up button is pressed
 */
internal data class TaskDetailActions(
    val onTitleChange: (String) -> Unit = {},
    val onDescriptionChange: (String) -> Unit = {},
    val onCategoryChange: (CategoryId) -> Unit = {},
    val onAlarmChange: (LocalDateTime?) -> Unit = {},
    val onIntervalChange: (AlarmInterval) -> Unit = {},
    val hasExactAlarmPermission: () -> Boolean = { false },
    val openExactAlarmPermissionScreen: () -> Unit = {},
    val openAppSettingsScreen: () -> Unit = {},
    val onUpPress: () -> Unit = {},
)
