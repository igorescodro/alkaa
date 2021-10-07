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
import com.escodro.alarmapi.AlarmPermission
import com.escodro.domain.interactor.AlarmInteractor
import com.escodro.domain.interactor.NotificationInteractor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Alarm dependency injection module.
 */
val alarmModule = module {

    factory { TaskNotificationScheduler(androidContext()) }
    factory { TaskNotificationChannel(androidContext()) }
    factory { TaskNotification(androidContext(), get()) }

    factory { TaskMapper() }

    factory<AlarmInteractor> { AlarmInteractorImpl(get()) }
    factory<NotificationInteractor> { NotificationInteractorImpl(get(), get()) }

    factory<AndroidVersion> { AndroidVersionImpl() }
    factory<AlarmPermission> { AlarmPermissionImpl(get(), get()) }
}
