package com.escodro.alarm.di

import com.escodro.alarm.interactor.AlarmInteractorImpl
import com.escodro.alarm.interactor.NotificationInteractorImpl
import com.escodro.domain.interactor.AlarmInteractor
import com.escodro.domain.interactor.NotificationInteractor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class AlarmModule {

    @Binds
    abstract fun bindAlarmInteractor(impl: AlarmInteractorImpl): AlarmInteractor

    @Binds
    abstract fun bindNotificationInteractor(
        impl: NotificationInteractorImpl
    ): NotificationInteractor
}
