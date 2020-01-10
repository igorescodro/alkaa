package com.escodro.alkaa.ui

import androidx.test.uiautomator.By
import androidx.test.uiautomator.BySelector
import androidx.test.uiautomator.UiObject2
import androidx.test.uiautomator.UiSelector
import androidx.test.uiautomator.Until
import com.escodro.alkaa.R
import com.escodro.alkaa.framework.AcceptanceTest
import com.escodro.alkaa.framework.extension.waitForLauncher
import com.escodro.alkaa.presentation.MainActivity
import com.escodro.domain.usecase.alarm.ScheduleAlarm
import com.escodro.local.model.Task
import java.util.Calendar
import java.util.regex.Pattern
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.koin.test.inject

/**
 * Test class to validate the notification flow.
 */
class NotificationTest : AcceptanceTest<MainActivity>(MainActivity::class.java) {

    private val scheduleAlarm: ScheduleAlarm by inject()

    private val appName by lazy { context.getString(R.string.app_name) }

    @After
    fun clearTable() = runBlocking {
        daoProvider.getTaskDao().cleanTable()
    }

    @Test
    fun validateTaskNotification() {
        val taskName = "SOMEBODY RING THE ALARM"
        insertTask(taskName)
        goToNotificationDrawer()
        validateNotificationContent(taskName)
        clearAllNotifications()
    }

    @Test
    fun validateTaskUpdate() = runBlocking {
        val task = insertTask("Hi, I'm a PC")
        val updatedTitle = "Hi, I'm a Mac"
        task.title = updatedTitle
        daoProvider.getTaskDao().updateTask(task)

        goToNotificationDrawer()
        validateNotificationContent(updatedTitle)
        clearAllNotifications()
    }

    @Test
    fun validateCompletedTask() = runBlocking {
        val taskName = "I should not be seen"
        val task = insertTask(taskName)
        task.completed = true
        daoProvider.getTaskDao().updateTask(task)

        goToNotificationDrawer()
        validateNotificationNotShown(taskName)
    }

    @Test
    fun validateDeletedTask() = runBlocking {
        val taskName = "Ops, I did it again..."
        val task = insertTask(taskName)
        daoProvider.getTaskDao().deleteTask(task)

        goToNotificationDrawer()
        validateNotificationNotShown(taskName)
    }

    @Test
    fun validateTaskNotificationFlow() {
        val taskName = "Hey Jude"
        insertTask(taskName)
        goToNotificationDrawer()
        validateNotificationContent(taskName)
        uiDevice.findObject(By.text(taskName)).click()
        checkThat.viewHasText(R.id.edittext_taskdetail_title, taskName)
    }

    @Test
    fun completeTaskViaNotification() {
        val taskName = "Sonic, gotta go fast!"
        insertTask(taskName)
        goToNotificationDrawer()
        validateNotificationContent(taskName)
        val doneButton = byTextIgnoreCase(context.getString(R.string.notification_action_completed))
        uiDevice.findObject(doneButton).click()
        reopenApp()
        events.waitFor(1000)
        checkThat.listNotContainsItem(R.id.recyclerview_tasklist_list, taskName)
        openDrawer()
        events.clickOnViewWithText(R.string.drawer_menu_completed_tasks)
        checkThat.viewHasText(R.id.toolbar_title, R.string.drawer_menu_completed_tasks)
        checkThat.listContainsItem(R.id.recyclerview_tasklist_list, taskName)
    }

    @Test
    fun snoozeTaskViaNotification() {
        val taskName = "Sorry, I need to sleep more"
        insertTask(taskName)
        goToNotificationDrawer()
        validateNotificationContent(taskName)
        val doneButton = byTextIgnoreCase(context.getString(R.string.notification_action_snooze))
        uiDevice.findObject(doneButton).click()
        reopenApp()
        checkThat.listContainsItem(R.id.recyclerview_tasklist_list, taskName)
        events.clickOnRecyclerItem(R.id.recyclerview_tasklist_list)

        val delayedCalendar = Calendar.getInstance()
        delayedCalendar.add(Calendar.MINUTE, 15)
        delayedCalendar.set(Calendar.SECOND, 0)
        delayedCalendar.set(Calendar.MILLISECOND, 0)
        checkThat.viewHasDate(R.id.chip_taskdetail_date, delayedCalendar)
    }

    private fun insertTask(taskName: String) = runBlocking {
        with(Task(id = 15, title = taskName)) {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.SECOND, 3)
            dueDate = calendar
            daoProvider.getTaskDao().insertTask(this)
            scheduleAlarm(this.id, this.dueDate?.time?.time)
            this
        }
    }

    private fun goToNotificationDrawer() {
        uiDevice.pressHome()
        uiDevice.openNotification()
        uiDevice.wait(Until.hasObject(By.pkg("com.android.systemui")), 1000)
    }

    private fun validateNotificationContent(taskName: String) {
        uiDevice.wait(Until.hasObject(By.text(appName)), 5000)
        val title = uiDevice.findObject(By.text(appName))
        val text = uiDevice.findObject(By.text(taskName))
        assertEquals(appName, title.text)
        assertEquals(taskName, text.text)
    }

    private fun validateNotificationNotShown(taskName: String) {
        uiDevice.wait(Until.hasObject(By.text(appName)), 5000)
        val title = uiDevice.findObject(By.text(appName))
        val text = uiDevice.findObject(By.text(taskName))
        assertNull(title)
        assertNull(text)
    }

    private fun clearAllNotifications() {
        goToNotificationDrawer()
        val clearAll: UiObject2 =
            uiDevice.findObject(By.res("com.android.systemui:id/dismiss_text"))
        clearAll.click()
    }

    private fun reopenApp() {
        uiDevice.pressRecentApps()
        uiDevice.waitForLauncher()
        uiDevice.findObject(UiSelector().descriptionContains(context.getString(R.string.app_name)))
            .click()
    }

    private fun openDrawer() {
        events.openDrawer(R.id.drawer_layout_main_parent)
        checkThat.drawerIsOpen(R.id.drawer_layout_main_parent)
    }

    private fun byTextIgnoreCase(text: String): BySelector {
        val pattern = Pattern.compile(text, Pattern.CASE_INSENSITIVE)
        return By.text(pattern)
    }
}
