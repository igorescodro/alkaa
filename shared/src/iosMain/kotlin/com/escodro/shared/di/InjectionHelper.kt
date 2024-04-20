package com.escodro.shared.di

import com.escodro.alarm.notification.NotificationActionDelegate
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Helper class to inject dependencies in the iOS platform.
 */
class InjectionHelper : KoinComponent {

    /**
     * Provides the [NotificationActionDelegate] instance.
     */
    val notificationActionDelegate: NotificationActionDelegate by inject()
}
