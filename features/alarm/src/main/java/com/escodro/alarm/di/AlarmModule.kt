package com.escodro.alarm.di

import com.escodro.alarm.interactor.AlarmInteractorImpl
import com.escodro.alarm.interactor.NotificationInteractorImpl
import com.escodro.alarm.mapper.TaskMapper
import com.escodro.alarm.notification.TaskNotification
import com.escodro.alarm.notification.TaskNotificationChannel
import com.escodro.alarm.notification.TaskNotificationScheduler
import com.escodro.alarm.permission.AlarmPermissionImpl
import com.escodro.alarm.permission.AndroidVersion
import com.escodro.alarm.permission.AndroidVersionImpl
import com.escodro.alarm.permission.PermissionChecker
import com.escodro.alarm.permission.PermissionCheckerImpl
import com.escodro.alarmapi.AlarmPermission
import com.escodro.domain.interactor.AlarmInteractor
import com.escodro.domain.interactor.NotificationInteractor
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * Alarm dependency injection module.
 */
val alarmModule = module {

    factoryOf(::TaskNotificationScheduler)
    factoryOf(::TaskNotificationChannel)
    factoryOf(::TaskNotification)

    factoryOf(::TaskMapper)

    factoryOf(::AlarmInteractorImpl) bind AlarmInteractor::class
    factoryOf(::NotificationInteractorImpl) bind NotificationInteractor::class

    factoryOf(::AndroidVersionImpl) bind AndroidVersion::class
    factoryOf(::PermissionCheckerImpl) bind PermissionChecker::class
    factoryOf(::AlarmPermissionImpl) bind AlarmPermission::class
}
