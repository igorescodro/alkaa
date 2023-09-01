package com.escodro.alkaa

import android.Manifest
import android.app.Notification
import androidx.annotation.StringRes
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import com.escodro.alkaa.fake.FAKE_TASK
import com.escodro.alkaa.navigation.NavGraph
import com.escodro.alkaa.util.WindowSizeClassFake
import com.escodro.core.extension.getNotificationManager
import com.escodro.core.extension.toLocalDateTime
import com.escodro.domain.usecase.alarm.ScheduleAlarm
import com.escodro.local.Task
import com.escodro.local.dao.TaskDao
import com.escodro.local.model.AlarmInterval
import com.escodro.test.rule.DisableAnimationsRule
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import java.util.Calendar
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import com.escodro.alarm.R as AlarmR

@OptIn(ExperimentalTestApi::class)
internal class NotificationFlowTest : KoinTest {

    private val taskDao: TaskDao by inject()

    private val scheduleAlarm: ScheduleAlarm by inject()

    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val disableAnimationsRule = DisableAnimationsRule()

    @get:Rule
    val runtimePermissionRule = GrantPermissionRule.grant(Manifest.permission.POST_NOTIFICATIONS)

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setup() {
        // Clean all existing tasks and categories
        runTest {
            taskDao.cleanTable()
        }
        composeTestRule.setContent {
            NavGraph(windowSizeClass = WindowSizeClassFake.Phone)
        }
    }

    @After
    fun tearDown() {
        context.getNotificationManager()?.cancelAll()
    }

    @Test
    fun test_notificationIsShown() = runTest {
        // Insert a task
        val id = 12L
        val name = "Don't believe me?"
        val appName = context.getString(com.escodro.core.R.string.app_name)
        insertTask(id, name)

        // Wait until the notification is launched
        val notificationManager = context.getNotificationManager()
        composeTestRule.waitUntil(10_000) { notificationManager!!.activeNotifications.isNotEmpty() }

        // Validate the notification info
        with(notificationManager!!.activeNotifications.first()) {
            assertEquals(id.toInt(), this.id)
            assertEquals(name, this.notification.extras.getString(Notification.EXTRA_TEXT))
            assertEquals(appName, this.notification.extras.getString(Notification.EXTRA_TITLE))
        }
    }

    @Test
    fun test_whenNotificationIsClickedOpensTaskDetails() = runTest {
        // Insert a task
        val id = 13L
        val name = "Click here for a surprise"
        insertTask(id, name)

        // Wait until the notification is launched
        val notificationManager = context.getNotificationManager()
        composeTestRule.waitUntil(10_000) { notificationManager!!.activeNotifications.isNotEmpty() }

        // Run the PendingIntent in the notification
        notificationManager!!.activeNotifications.first().notification.contentIntent.send()

        // Wait until the title is displayed
        composeTestRule.waitUntilAtLeastOneExists(hasText(name))

        // Validate the task detail was opened
        composeTestRule.onNodeWithText(name).assertIsDisplayed()
        composeTestRule
            .onNodeWithContentDescription(
                "Back",
                useUnmergedTree = true,
            )
            .assertIsDisplayed()
    }

    @Test
    fun test_taskUpdateReflectsInNotification() = runTest {
        // Insert and update a task
        val task = insertTask(name = "Hi, I'm a PC")
        val updatedTitle = "Hi, I'm a Mac"

        val updatedTask = task.copy(task_title = updatedTitle)
        taskDao.updateTask(updatedTask)

        // Wait until the notification is launched
        val notificationManager = context.getNotificationManager()
        composeTestRule.waitUntil(10_000) { notificationManager!!.activeNotifications.isNotEmpty() }

        // Validate the notification has the updated title
        with(notificationManager!!.activeNotifications.first()) {
            assertEquals(updatedTitle, this.notification.extras.getString(Notification.EXTRA_TEXT))
        }
    }

    @Test
    fun test_taskCompletedIsNotNotified() = runTest {
        // Insert a task and updated it as "completed"
        val task = insertTask(name = "Shhh! I wasn't here!")

        val updatedTask = task.copy(task_is_completed = true)
        taskDao.updateTask(updatedTask)

        // Wait for 7 seconds
        val notificationManager = context.getNotificationManager()

        // Validate the notification was not launched
        assertTrue(notificationManager!!.activeNotifications.isEmpty())
    }

    @Test
    fun test_completeTaskViaNotification() = runTest {
        // Insert a task
        val id = 3333L
        val name = "You complete me!"
        insertTask(id = id, name = name)

        // Wait until the notification is launched
        val notificationManager = context.getNotificationManager()
        composeTestRule.waitUntil(10_000) { notificationManager!!.activeNotifications.isNotEmpty() }

        // Run the PendingIntent in the "Done" action button
        notificationManager!!.activeNotifications.first().notification.actions[1].actionIntent.send()

        // Wait until the task is complete and no longer visible
        composeTestRule.waitUntilDoesNotExist(hasText(name))

        // Validate the task is now updated as "completed"
        val task = taskDao.getTaskById(id)

        assertTrue(task!!.task_is_completed)
    }

    @Test
    fun test_snoozeTaskViaNotification() = runTest {
        // Insert a task
        val id = 9999L
        val name = "I need to sleep more..."
        val calendar = Calendar.getInstance()
        insertTask(id = id, name = name, calendar = calendar)

        // Wait until the notification is launched
        val notificationManager = context.getNotificationManager()
        composeTestRule.waitUntil(10_000) { notificationManager!!.activeNotifications.isNotEmpty() }

        // Run the PendingIntent in the "Snooze" action button
        notificationManager!!.activeNotifications.first().notification.actions[0].actionIntent.send()
    }

    @Test
    fun test_ifRepeatingTaskDoesNotHaveDoneButton() {
        // Insert a repeating task
        insertRepeatingTask(name = "WE ARE NOT DONE YET, YOUNG LADY")

        // Wait until the notification is launched
        val notificationManager = context.getNotificationManager()
        composeTestRule.waitUntil(10_000) { notificationManager!!.activeNotifications.isNotEmpty() }

        // Validate it only have one action and it is "Snooze"
        val notification = notificationManager!!.activeNotifications.first().notification
        val snoozeString = context.getString(AlarmR.string.notification_action_snooze)
        assertEquals(1, notification.actions.size)
        assertEquals(snoozeString, notification.actions.first().title)
    }

    @Test
    fun test_ifNonRepeatingTaskDoesNotHaveBothButtons() = runTest {
        // Insert a normal task
        insertTask(name = "The way it is")

        // Insert a repeating task
        val notificationManager = context.getNotificationManager()
        composeTestRule.waitUntil(10_000) { notificationManager!!.activeNotifications.isNotEmpty() }

        // Validate it only have two actions: "Snooze" and "Done"
        val notification = notificationManager!!.activeNotifications.first().notification
        val snoozeString = context.getString(AlarmR.string.notification_action_snooze)
        val doneString = context.getString(AlarmR.string.notification_action_completed)
        assertEquals(2, notification.actions.size)
        assertEquals(snoozeString, notification.actions.first().title)
        assertEquals(doneString, notification.actions.last().title)
    }

    private fun string(@StringRes resId: Int): String =
        context.getString(resId)

    private suspend fun insertTask(
        id: Long = 15L,
        name: String,
        calendar: Calendar = Calendar.getInstance(),
    ): Task {
        val dueDate = calendar.apply { add(Calendar.SECOND, 3) }.toLocalDateTime()
        return with(FAKE_TASK.copy(task_id = id, task_title = name, task_due_date = dueDate)) {
            taskDao.insertTask(this)
            scheduleAlarm(this.task_id, this.task_due_date!!)
            this
        }
    }

    private fun insertRepeatingTask(name: String) = runTest {
        val dueDate = Calendar.getInstance().apply { add(Calendar.SECOND, 1) }.toLocalDateTime()

        with(
            FAKE_TASK.copy(
                task_id = 1000,
                task_title = name,
                task_due_date = dueDate,
                task_is_repeating = true,
                task_alarm_interval = AlarmInterval.HOURLY,
            ),
        ) {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.SECOND, 2)
            taskDao.insertTask(this)
            scheduleAlarm(this.task_id, this.task_due_date!!)
        }
    }
}
