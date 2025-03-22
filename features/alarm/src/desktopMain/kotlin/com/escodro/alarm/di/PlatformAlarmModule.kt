package com.escodro.alarm.di

import com.escodro.alarm.notification.DesktopNotificationScheduler
import com.escodro.alarm.notification.DesktopTaskNotification
import com.escodro.alarm.notification.NotificationScheduler
import com.escodro.alarm.notification.TaskNotification
import com.escodro.alarm.permission.DesktopAlarmPermission
import com.escodro.alarmapi.AlarmPermission
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformAlarmModule = module {
    factoryOf(::DesktopNotificationScheduler) bind NotificationScheduler::class
    factoryOf(::DesktopTaskNotification) bind TaskNotification::class
    factoryOf(::DesktopAlarmPermission) bind AlarmPermission::class
}
