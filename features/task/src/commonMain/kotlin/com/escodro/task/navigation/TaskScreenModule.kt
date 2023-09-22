package com.escodro.task.navigation

import cafe.adriel.voyager.core.registry.screenModule
import com.escodro.navigation.AlkaaDestinations

/**
 * Task screen navigation module.
 */
val taskScreenModule = screenModule {
    register<AlkaaDestinations.Task.TaskDetail> { provider ->
        TaskDetailScreen(provider.taskId)
    }
    register<AlkaaDestinations.Task.AddTaskBottomSheet> {
        AddTaskBottomSheet()
    }
}
