package com.escodro.alarm.di

import com.escodro.alarm.interactor.AlarmInteractorImpl
import com.escodro.alarm.interactor.NotificationInteractorImpl
import com.escodro.alarm.mapper.TaskMapper
import com.escodro.domain.interactor.AlarmInteractor
import com.escodro.domain.interactor.NotificationInteractor
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * Alarm dependency injection module.
 */
val alarmModule = module {

    factoryOf(::TaskMapper)

    factoryOf(::AlarmInteractorImpl) bind AlarmInteractor::class
    factoryOf(::NotificationInteractorImpl) bind NotificationInteractor::class

    includes(platformAlarmModule)
}

expect val platformAlarmModule: Module
