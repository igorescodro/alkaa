package com.escodro.alkaa

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import com.escodro.alarm.notification.NotificationScheduler
import com.escodro.alarm.notification.TaskNotification
import com.escodro.alkaa.test.platformModule
import com.escodro.alkaa.test.uiTest
import com.escodro.home.presentation.HomeSection
import com.escodro.resources.provider.ResourcesProvider
import com.escodro.shared.di.appModules
import dev.icerock.moko.resources.desc.desc
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
internal class HomeScreenTest {

    private val resourcesProvider = ResourcesProvider()

    @BeforeTest
    fun setup() {
        startKoin {
            modules(
                platformModule + appModules + module {
                    // Mocks the notification and alarm modules - fails on iOS
                    single<NotificationScheduler> { NotificationSchedulerFake() }
                    single<TaskNotification> { TaskNotificationFake() }
                },
            )
        }
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun test_titleChangesWhenBottomIconIsSelected() = uiTest {
        HomeSection.entries.forEach { section ->
            val title = resourcesProvider.getString(section.title.desc())

            // Click on each item and validate the title
            onNodeWithContentDescription(label = title, useUnmergedTree = true).performClick()
            onAllNodesWithText(title)[0].assertExists()
        }
    }
}
