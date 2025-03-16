package com.escodro.task.presentation.detail.main

import androidx.compose.runtime.Composable
import com.escodro.taskapi.TaskDetailScreen

internal class TaskDetailScreenImpl : TaskDetailScreen {

    @Composable
    override fun Content(taskId: Long, onUpPress: () -> Unit) {
        TaskDetailScreen(
            taskId = taskId,
            onUpPress = onUpPress,
        )
    }
}
