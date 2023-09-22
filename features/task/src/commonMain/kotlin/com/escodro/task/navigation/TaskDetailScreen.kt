package com.escodro.task.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.escodro.task.presentation.detail.main.TaskDetailScreen

/**
 * Task detail screen.
 */
internal data class TaskDetailScreen(val taskId: Long) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        TaskDetailScreen(
            taskId = taskId,
            onUpPress = { navigator.pop() }
        )
    }
}
