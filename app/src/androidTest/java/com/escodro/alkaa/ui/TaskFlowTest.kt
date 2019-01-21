package com.escodro.alkaa.ui

import androidx.test.uiautomator.UiSelector
import com.escodro.alkaa.R
import com.escodro.alkaa.data.local.model.Category
import com.escodro.alkaa.framework.AcceptanceTest
import com.escodro.alkaa.framework.extension.waitForLaucher
import com.escodro.alkaa.ui.main.MainActivity
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.Calendar

/**
 * Test class to validate Task screen and flow.
 */
class TaskFlowTest : AcceptanceTest<MainActivity>(MainActivity::class.java) {

    @Before
    fun addCategories() {
        daoProvider.getCategoryDao().insertCategory(Category(name = "Books", color = "#cc5a71"))
        daoProvider.getCategoryDao().insertCategory(Category(name = "Music", color = "#58a4b0"))
        daoProvider.getCategoryDao().insertCategory(Category(name = "Shared", color = "#519872"))
    }

    @After
    fun cleanTable() {
        daoProvider.getTaskDao().cleanTable()
        daoProvider.getCategoryDao().cleanTable()
    }

    @Test
    fun areAllViewsIsCompletelyDisplayed() {
        checkThat.viewIsCompletelyDisplayed(R.id.recyclerview_tasklist_list)
    }

    @Test
    fun areAllDetailViewsIsCompletelyDisplayed() {
        addAndOpenTask("everybody dance now")
        scheduleTask(1993, 3, 15, 16, 2)
        checkThat.viewIsCompletelyDisplayed(R.id.edittext_taskdetail_title)
        checkThat.viewIsCompletelyDisplayed(R.id.srg_taskdetail_list)
        checkThat.viewIsCompletelyDisplayed(R.id.edittext_taskdetail_description)
        checkThat.viewIsCompletelyDisplayed(R.id.chip_taskdetail_date)
        checkThat.viewIsCompletelyDisplayed(R.id.btn_taskdetail_date)
    }

    @Test
    fun isDescriptionSingleLine() {
        addTask(
            "Lorem ipsum dolor sit amet, te elit possit suavitate duo. Nec sale sonet" +
                " scriptorem ei, option prompta ut sed. At everti discere oportere sea."
        )
        checkThat.textHasFixedLines(R.id.textview_itemtask_description, 1)
    }

    @Test
    fun addNewTask() {
        addTask("buy milk")
    }

    @Test
    fun deleteTask() {
        val taskName = "eat vegetables"
        addTask(taskName)
        events.longPressOnRecyclerItem(R.id.recyclerview_tasklist_list)
        events.clickDialogOption(R.array.task_dialog_options, 0)
        events.waitFor(R.id.recyclerview_tasklist_list, 1000)
        checkThat.listNotContainsItem(R.id.recyclerview_tasklist_list, taskName)
    }

    @Test
    fun checkTaskAsCompleted() {
        addTask("write article")
        events.clickOnView(R.id.checkbox_itemtask_completed)
        checkThat.checkBoxIsChecked(R.id.checkbox_itemtask_completed)
    }

    @Test
    fun addAndOpenTask() {
        addAndOpenTask("select a new category")
    }

    @Test
    fun renameTask() {
        val taskUpdated = "fallin' in love with me"
        addAndOpenTask("one kiss is all it takes")
        events.clickOnView(R.id.edittext_taskdetail_title)
        events.textOnView(R.id.edittext_taskdetail_title, taskUpdated)
        events.waitFor(R.id.recyclerview_tasklist_list, 500)
        events.navigateUp()
        events.waitFor(R.id.recyclerview_tasklist_list, 2000)
        checkThat.listContainsItem(R.id.recyclerview_tasklist_list, taskUpdated)
        events.clickOnRecyclerItem(R.id.recyclerview_tasklist_list)
        checkThat.viewHasText(R.id.edittext_taskdetail_title, taskUpdated)
    }

    @Test
    fun updateTitleWithEmptyText() {
        val task = "don't you worry child"
        addAndOpenTask(task)
        events.clickOnView(R.id.edittext_taskdetail_title)
        events.textOnView(R.id.edittext_taskdetail_title, "")
        events.waitFor(R.id.recyclerview_tasklist_list, 500)
        events.navigateUp()
        events.waitFor(R.id.recyclerview_tasklist_list, 2000)
        checkThat.listContainsItem(R.id.recyclerview_tasklist_list, task)
    }

    @Test
    fun checkIfTaskCategoryIsSaved() {
        addAndOpenTask("call my by your name")
        events.clickOnRadioButton(R.id.srg_radiogroup_list, 1)
        events.navigateUp()
        events.clickOnRecyclerItem(R.id.recyclerview_tasklist_list)
        checkThat.radioButtonIsChecked(R.id.srg_radiogroup_list, 1)
    }

    @Test
    fun checkIfDisplayDueDateIsCorrect() {
        addAndOpenTask("register to vote")
        scheduleTask(2018, 10, 11, 16, 32)
    }

    @Test
    fun checkIfAlarmIsRemoved() {
        addAndOpenTask("cancel dinner")
        scheduleTask(2020, 2, 2, 22, 15)
        events.clickOnCloseChip(R.id.chip_taskdetail_date)
        checkThat.viewHasText(R.id.chip_taskdetail_date, "")
    }

    @Test
    fun addDescription() {
        val description = "- call me by you name\n- never let me go\n- love, simon"
        addAndOpenTask("my book list")
        events.textOnView(R.id.edittext_taskdetail_description, description)
        events.waitFor(R.id.recyclerview_tasklist_list, 500)
        events.navigateUp()
        events.waitFor(R.id.recyclerview_tasklist_list, 2000)
        events.clickOnRecyclerItem(R.id.recyclerview_tasklist_list)
        checkThat.viewHasText(R.id.edittext_taskdetail_description, description)
    }

    @Test
    fun checkFocusWhenClickingOnAddIcon() {
        events.clickOnView(R.id.imageview_itemadd_completed)
        checkThat.viewHasFocus(R.id.edittext_itemadd_description)
    }

    @Test
    fun addTaskAfterLeaveScreen() {
        uiDevice.pressHome()
        uiDevice.pressRecentApps()
        uiDevice.waitForLaucher()
        uiDevice.findObject(UiSelector().descriptionContains(context.getString(R.string.app_name)))
            .click()

        addTask("It must reflect on UI")
    }

    private fun addTask(taskName: String) {
        events.clickOnView(R.id.edittext_itemadd_description)
        events.textOnView(R.id.edittext_itemadd_description, taskName)
        events.pressImeActionButton(R.id.edittext_itemadd_description)
        events.waitFor(R.id.recyclerview_tasklist_list, 2000)
        checkThat.listContainsItem(R.id.recyclerview_tasklist_list, taskName)
    }

    private fun addAndOpenTask(taskName: String) {
        addTask(taskName)
        events.clickOnRecyclerItem(R.id.recyclerview_tasklist_list)
        checkThat.viewHasText(R.id.edittext_taskdetail_title, taskName)
        checkThat.viewHasText(R.id.toolbar_title, "")
    }

    private fun scheduleTask(year: Int, month: Int, day: Int, hour: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day, hour, minute, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        events.clickOnView(R.id.btn_taskdetail_date)
        events.setDate(calendar)
        events.clickOnView(android.R.id.button1)
        events.setTime(calendar)
        events.clickOnView(android.R.id.button1)
        checkThat.viewHasDate(R.id.chip_taskdetail_date, calendar)
    }
}
