package com.escodro.alarm.di

import com.escodro.alarm.notification.AndroidNotificationScheduler
import com.escodro.alarm.notification.AndroidTaskNotification
import com.escodro.alarm.notification.NotificationScheduler
import com.escodro.alarm.notification.TaskNotification
import com.escodro.alarm.notification.TaskNotificationChannel
import com.escodro.alarm.permission.AndroidAlarmPermission
import com.escodro.alarm.permission.AndroidPermissionChecker
import com.escodro.alarm.permission.AndroidSdkVersion
import com.escodro.alarm.permission.PermissionChecker
import com.escodro.alarm.permission.ScreenNavigator
import com.escodro.alarm.permission.ScreenNavigatorImpl
import com.escodro.alarm.permission.SdkVersion
import com.escodro.alarmapi.AlarmPermission
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformAlarmModule: Module = module {
    factoryOf(::AndroidNotificationScheduler) bind NotificationScheduler::class
    factoryOf(::AndroidTaskNotification) bind TaskNotification::class
    factoryOf(::TaskNotificationChannel)

    factoryOf(::AndroidSdkVersion) bind SdkVersion::class
    factoryOf(::AndroidPermissionChecker) bind PermissionChecker::class
    factoryOf(::AndroidAlarmPermission) bind AlarmPermission::class

    factoryOf(::ScreenNavigatorImpl) bind ScreenNavigator::class
}
