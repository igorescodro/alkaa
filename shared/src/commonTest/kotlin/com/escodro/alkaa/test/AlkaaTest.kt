package com.escodro.alkaa.test

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.escodro.alarm.notification.NotificationScheduler
import com.escodro.alarm.notification.TaskNotification
import com.escodro.alkaa.fake.NotificationSchedulerFake
import com.escodro.alkaa.fake.OpenAlarmSchedulerFake
import com.escodro.alkaa.fake.TaskNotificationFake
import com.escodro.appstate.rememberAlkaaAppState
import com.escodro.shared.AlkaaMultiplatformApp
import com.escodro.shared.di.appModules
import com.escodro.task.presentation.detail.alarm.interactor.OpenAlarmScheduler
import com.escodro.test.retry
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.test.KoinTest
import org.koin.test.mock.declare

/**
 * Setup the Koin dependency injection and disable animations before the test.
 */
fun KoinTest.beforeTest() {
    startKoin { modules(platformModule + appModules) }
    declare<NotificationScheduler> { NotificationSchedulerFake() }
    declare<TaskNotification> { TaskNotificationFake() }
    declare<OpenAlarmScheduler> { OpenAlarmSchedulerFake() }
}

/**
 * Stop the Koin dependency injection and enable animations after the test.
 */
fun afterTest() {
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
        CompositionLocalProvider(
            LocalLifecycleOwner provides LocalLifecycleOwnerFake(),
        ) {
            AlkaaMultiplatformApp(appState = rememberAlkaaAppState())
        }
    }
    block()
}

/**
 * Run a flaky test in the UI context, retrying it up to a maximum number of attempts if it fails.
 *
 * @param times the maximum number of times the test should be executed (initial attempt plus retries)
 * @param block the test to be executed
 */
@OptIn(ExperimentalTestApi::class)
fun flakyUiTest(
    times: Int = 5,
    block: ComposeUiTest.() -> Unit,
) = retry(times) {
    uiTest(block)
}
/**
 * Fake implementation of [LifecycleOwner] to be used in the tests, specially in iOS ones.
 */
private class LocalLifecycleOwnerFake : LifecycleOwner {
    override val lifecycle: Lifecycle = LifecycleRegistry(this).apply {
        currentState = Lifecycle.State.RESUMED
    }
}

/**
 * Koin module to provide the platform dependencies.
 */
expect val platformModule: Module
