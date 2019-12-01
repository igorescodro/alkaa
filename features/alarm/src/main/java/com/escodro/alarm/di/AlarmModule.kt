package com.escodro.alarm.di

import com.escodro.alarm.TaskMapper
import com.escodro.alarm.notification.TaskNotification
import com.escodro.alarm.notification.TaskNotificationChannel
import com.escodro.alarm.notification.TaskNotificationScheduler
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val alarmModule = module {

    single { TaskNotificationScheduler(androidContext()) }
    single { TaskNotificationChannel(androidContext()) }
    single { TaskNotification(androidContext(), get()) }

    factory { TaskMapper() }
}
