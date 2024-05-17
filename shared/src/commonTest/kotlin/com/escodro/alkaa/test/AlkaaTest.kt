package com.escodro.alkaa.test

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.escodro.alarm.notification.NotificationScheduler
import com.escodro.alarm.notification.TaskNotification
import com.escodro.alkaa.fake.AppStateFake
import com.escodro.alkaa.fake.NotificationSchedulerFake
import com.escodro.alkaa.fake.OpenAlarmSchedulerFake
import com.escodro.alkaa.fake.TaskNotificationFake
import com.escodro.shared.AlkaaMultiplatformApp
import com.escodro.shared.di.appModules
import com.escodro.task.presentation.detail.alarm.interactor.OpenAlarmScheduler
import org.koin.compose.KoinContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.test.KoinTest
import org.koin.test.mock.declare

/**
 * Setup the Koin dependency injection and disable animations before the test.
 */
fun KoinTest.beforeTest() {
    PlatformAnimation().disable()
    startKoin { modules(platformModule + appModules) }
    declare<NotificationScheduler> { NotificationSchedulerFake() }
    declare<TaskNotification> { TaskNotificationFake() }
    declare<OpenAlarmScheduler> { OpenAlarmSchedulerFake() }
}

/**
 * Stop the Koin dependency injection and enable animations after the test.
 */
fun afterTest() {
    PlatformAnimation().enable()
    stopKoin()
}

/**
 * Run a test in the UI context, setting up the Koin modules and the Compose content.
 *
 * @param block the test to be executed
 */
@OptIn(ExperimentalTestApi::class)
fun uiTest(block: ComposeUiTest.() -> Unit) = runComposeUiTest {
    setContent {
        KoinContext { AlkaaMultiplatformApp(appState = AppStateFake()) }
    }
    block()
}

/**
 * Platform specific animation control for the test.
 */
expect class PlatformAnimation() {

    /**
     * Disable the animations.
     */
    fun disable()

    /**
     * Enable the animations.
     */
    fun enable()
}

/**
 * Koin module to provide the platform dependencies.
 */
expect val platformModule: Module
