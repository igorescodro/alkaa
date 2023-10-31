package com.escodro.alarm.di

import com.escodro.alarm.notification.IosNotificationScheduler
import com.escodro.alarm.notification.IosTaskNotification
import com.escodro.alarm.notification.NotificationScheduler
import com.escodro.alarm.notification.TaskNotification
import com.escodro.alarm.permission.IosAlarmPermission
import com.escodro.alarmapi.AlarmPermission
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformAlarmModule: Module = module {
    factoryOf(::IosNotificationScheduler) bind NotificationScheduler::class
    factoryOf(::IosTaskNotification) bind TaskNotification::class

    factoryOf(::IosAlarmPermission) bind AlarmPermission::class
}
