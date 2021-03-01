package com.escodro.task.presentation.detail

import com.escodro.task.model.AlarmInterval
import com.escodro.task.presentation.detail.main.CategoryId
import java.util.Calendar

internal data class TaskDetailActions(
    val onTitleChanged: (String) -> Unit = {},
    val onDescriptionChanged: (String) -> Unit = {},
    val onCategoryChanged: (CategoryId) -> Unit = {},
    val onAlarmUpdated: (Calendar?) -> Unit = {},
    val onIntervalSelected: (AlarmInterval) -> Unit = {}
)
