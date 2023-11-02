package com.escodro.domain.usecase.fake

import com.escodro.domain.interactor.NotificationInteractor
import com.escodro.domain.model.Task

internal class NotificationInteractorFake : NotificationInteractor {

    private val notificationMap: MutableMap<Long, Boolean> = mutableMapOf()

    override fun show(task: Task) {
        notificationMap[task.id] = true
    }

    override fun dismiss(task: Task) {
        notificationMap[task] = false
    }

    fun isNotificationShown(notificationId: Long): Boolean =
        notificationMap[notificationId] ?: false

    fun clear() {
        notificationMap.clear()
    }
}
