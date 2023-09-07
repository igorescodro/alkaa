package com.escodro.task.presentation.detail

import com.escodro.task.model.AlarmInterval
import com.escodro.task.presentation.detail.main.CategoryId
import kotlinx.datetime.LocalDateTime

internal data class TaskDetailActions(
    val onTitleChange: (String) -> Unit = {},
    val onDescriptionChange: (String) -> Unit = {},
    val onCategoryChange: (CategoryId) -> Unit = {},
    val onAlarmUpdate: (LocalDateTime?) -> Unit = {},
    val onIntervalSelect: (AlarmInterval) -> Unit = {},
    val hasAlarmPermission: () -> Boolean = { false },
    val shouldCheckNotificationPermission: Boolean = false,
    val onUpPress: () -> Unit = {},
)
