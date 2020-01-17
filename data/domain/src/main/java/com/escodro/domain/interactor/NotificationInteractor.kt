package com.escodro.domain.interactor

import com.escodro.domain.model.Task

/**
 * Interface to interact with the notification feature.
 */
interface NotificationInteractor {

    /**
     * Shows an notification.
     *
     * @param task task to be shown as notification
     */
    fun show(task: Task)

    /**
     * Dismisses the current notification.
     *
     * @param notificationId notification to be dismissed
     */
    fun dismiss(notificationId: Long)
}
