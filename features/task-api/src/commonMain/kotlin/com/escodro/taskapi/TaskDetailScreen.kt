package com.escodro.taskapi

import androidx.compose.runtime.Composable

interface TaskDetailScreen {

    @Composable
    fun Content(taskId: Long, onUpPress: () -> Unit)
}
