package com.escodro.alarm.notification

import com.escodro.coroutines.AppCoroutineScope
import com.escodro.domain.usecase.task.CompleteTask
import platform.Foundation.NSLog
import platform.UserNotifications.UNNotificationResponse

/**
 * Class to handle the notification actions in iOS.
 *
 * @param appCoroutineScope the app-wide coroutine scope
 * @param completeTaskUseCase the use case to complete a task
 */
class NotificationActionDelegate(
    private val appCoroutineScope: AppCoroutineScope,
    private val completeTaskUseCase: CompleteTask,
) {

    /**
     * Handles the notification response and actions.
     *
     * @param response the notification response
     * @param onCompletion the action to be executed after the task is completed
     */
    fun userNotificationCenter(response: UNNotificationResponse, onCompletion: () -> Unit) {
        NSLog("NotificationActionDelegate - userNotificationCenter")
        val content = response.notification.request.content
        NSLog("NotificationActionDelegate - content: $content")
        val taskId: Long = content.userInfo[IosNotificationScheduler.USER_INFO_TASK_ID] as? Long
            ?: return

        if (response.actionIdentifier == IosNotificationScheduler.ACTION_IDENTIFIER_DONE) {
            completeTask(taskId = taskId, onCompletion = onCompletion,)
        } else {
            NSLog("NotificationActionDelegate - Action not supported")
        }
    }

    private fun completeTask(taskId: Long, onCompletion: () -> Unit) {
        appCoroutineScope.launch {
            NSLog("NotificationActionDelegate - completeTask")
            completeTaskUseCase(taskId)
            onCompletion()
        }
    }
}
