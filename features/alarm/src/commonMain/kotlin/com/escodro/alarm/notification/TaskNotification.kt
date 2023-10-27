package com.escodro.alarm.notification

import com.escodro.alarm.model.Task

/**
 * Interface to show and dismiss notifications on each platform.
 */
internal interface TaskNotification {

    /**
     * Shows a notification with the given task. The UI might show complete task actions.
     */
    fun show(task: Task)

    /**
     * Shows a notification with the given task, with the respective UI for repeating tasks. The UI
     * might not show complete task actions.
     */
    fun showRepeating(task: Task)

    /**
     * Dismisses the notification with the given task id.
     */
    fun dismiss(taskId: Long)
}
