package com.escodro.alkaa

import android.app.Notification
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import com.escodro.alkaa.navigation.NavGraph
import com.escodro.core.extension.getNotificationManager
import com.escodro.designsystem.AlkaaTheme
import com.escodro.domain.usecase.alarm.ScheduleAlarm
import com.escodro.local.model.AlarmInterval
import com.escodro.local.model.Task
import com.escodro.local.provider.DaoProvider
import com.escodro.test.DisableAnimationsRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
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

@OptIn(ExperimentalCoroutinesApi::class)
internal class NotificationFlowTest : KoinTest {

    private val daoProvider: DaoProvider by inject()

    private val scheduleAlarm: ScheduleAlarm by inject()

    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val disableAnimationsRule = DisableAnimationsRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setup() {
        // Clean all existing tasks and categories
        runBlocking {
            daoProvider.getTaskDao().cleanTable()
        }
        composeTestRule.setContent {
            AlkaaTheme {
                NavGraph()
            }
        }
    }

    @After
    fun tearDown() {
        context.getNotificationManager()?.cancelAll()
    }

    @Test
    fun test_notificationIsShown() {
        val id = 12L
        val name = "Don't believe me?"
        val appName = context.getString(com.escodro.core.R.string.app_name)
        insertTask(id, name)

        val notificationManager = context.getNotificationManager()
        require(notificationManager != null)
        composeTestRule.waitUntil(10_000) { notificationManager.activeNotifications.isNotEmpty() }

        with(notificationManager.activeNotifications.first()) {
            assertEquals(id.toInt(), this.id)
            assertEquals(name, this.notification.extras[Notification.EXTRA_TEXT])
            assertEquals(appName, this.notification.extras[Notification.EXTRA_TITLE])
        }
    }

    @Test
    fun test_taskUpdateReflectsInNotification() = runBlocking {
        val task = insertTask(name = "Hi, I'm a PC")
        val updatedTitle = "Hi, I'm a Mac"

        val updatedTask = task.copy(title = updatedTitle)
        daoProvider.getTaskDao().updateTask(updatedTask)

        val notificationManager = context.getNotificationManager()
        require(notificationManager != null)
        composeTestRule.waitUntil(10_000) { notificationManager.activeNotifications.isNotEmpty() }

        with(notificationManager.activeNotifications.first()) {
            assertEquals(updatedTitle, this.notification.extras[Notification.EXTRA_TEXT])
        }
    }

    @Test
    fun test_taskCompletedIsNotNotified() = runBlocking {
        val task = insertTask(name = "Shhh! I wasn't here!")

        val updatedTask = task.copy(completed = true)
        daoProvider.getTaskDao().updateTask(updatedTask)

        val notificationManager = context.getNotificationManager()
        require(notificationManager != null)
        Thread.sleep(7_000)

        assertTrue(notificationManager.activeNotifications.isEmpty())
    }

    @Test
    fun test_completeTaskViaNotification() = runBlocking {
        val id = 3333L
        val name = "You complete me!"
        insertTask(id = id, name = name)

        val notificationManager = context.getNotificationManager()
        require(notificationManager != null)
        composeTestRule.waitUntil(10_000) { notificationManager.activeNotifications.isNotEmpty() }

        notificationManager.activeNotifications.first().notification.actions[1].actionIntent.send()

        Thread.sleep(300)
        val task = daoProvider.getTaskDao().getTaskById(id)

        assertTrue(task!!.completed)
    }

    @Test
    fun test_snoozeTaskViaNotification() = runBlocking {
        val id = 9999L
        val name = "I need to sleep more..."
        val calendar = Calendar.getInstance()
        insertTask(id = id, name = name, calendar = calendar)

        val notificationManager = context.getNotificationManager()
        require(notificationManager != null)
        composeTestRule.waitUntil(10_000) { notificationManager.activeNotifications.isNotEmpty() }

        notificationManager.activeNotifications.first().notification.actions[0].actionIntent.send()
    }

    @Test
    fun test_ifRepeatingTaskDoesNotHaveDoneButton() {
        insertRepeatingTask(name = "WE ARE NOT DONE YET, YOUNG LADY")

        val notificationManager = context.getNotificationManager()
        require(notificationManager != null)
        composeTestRule.waitUntil(10_000) { notificationManager.activeNotifications.isNotEmpty() }

        val notification = notificationManager.activeNotifications.first().notification
        val snoozeString = context.getString(AlarmR.string.notification_action_snooze)
        assertEquals(1, notification.actions.size)
        assertEquals(snoozeString, notification.actions.first().title)
    }

    @Test
    fun test_ifNonRepeatingTaskDoesNotHaveBothButtons() {
        insertTask(name = "The way it is")

        val notificationManager = context.getNotificationManager()
        require(notificationManager != null)
        composeTestRule.waitUntil(10_000) { notificationManager.activeNotifications.isNotEmpty() }

        val notification = notificationManager.activeNotifications.first().notification
        val snoozeString = context.getString(AlarmR.string.notification_action_snooze)
        val doneString = context.getString(AlarmR.string.notification_action_completed)
        assertEquals(2, notification.actions.size)
        assertEquals(snoozeString, notification.actions.first().title)
        assertEquals(doneString, notification.actions.last().title)
    }

    private fun insertTask(
        id: Long = 15L,
        name: String,
        calendar: Calendar = Calendar.getInstance()
    ): Task = runBlocking {
        with(Task(id = id, title = name)) {
            calendar.add(Calendar.SECOND, 1)
            dueDate = calendar
            daoProvider.getTaskDao().insertTask(this)
            scheduleAlarm(this.id, this.dueDate!!)
            this
        }
    }

    private fun insertRepeatingTask(name: String) = runBlocking {
        with(Task(id = 1000, title = name)) {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.SECOND, 2)
            dueDate = calendar
            isRepeating = true
            alarmInterval = AlarmInterval.HOURLY
            daoProvider.getTaskDao().insertTask(this)
            scheduleAlarm(this.id, this.dueDate!!)
            this
        }
    }
}
