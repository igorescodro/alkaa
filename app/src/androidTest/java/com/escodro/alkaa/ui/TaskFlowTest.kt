package com.escodro.alkaa.ui

import com.escodro.alkaa.R
import com.escodro.alkaa.data.local.model.Category
import com.escodro.alkaa.framework.AcceptanceTest
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
        mDaoProvider.getCategoryDao().insertCategory(Category("Books", "#cc5a71"))
        mDaoProvider.getCategoryDao().insertCategory(Category("Music", "#58a4b0"))
        mDaoProvider.getCategoryDao().insertCategory(Category("Shared", "#519872"))
    }

    @After
    fun cleanTable() {
        mDaoProvider.getTaskDao().cleanTable()
        mDaoProvider.getCategoryDao().cleanTable()
    }

    @Test
    fun areAllViewsIsCompletelyDisplayed() {
        checkThat.viewIsCompletelyDisplayed(R.id.edittext_tasklist_description)
        checkThat.viewIsCompletelyDisplayed(R.id.recyclerview_tasklist_list)
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
    fun addEmptyTask() {
        events.clickOnView(R.id.edittext_tasklist_description)
        events.pressImeActionButton(R.id.edittext_tasklist_description)
        checkThat.viewContainsError(R.id.edittext_tasklist_description, R.string.task_error_empty)
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
    fun checkIfAlarmIndicatorIsShown() {
        addAndOpenTask("buy a new calendar")
        scheduleTask(2018, 12, 25, 23, 59)
        events.navigateUp()
        checkThat.viewIsCompletelyDisplayed(R.id.imageview_itemtask_alarm)
    }

    @Test
    fun checkIfAlarmIsRemoved() {
        addAndOpenTask("cancel dinner")
        scheduleTask(2020, 2, 2, 22, 15)
        events.clickOnView(R.id.btn_taskdetail_remove_alarm)
        checkThat.viewHasText(R.id.textview_taskdetail_date, "")
    }

    private fun addTask(taskName: String) {
        events.clickOnView(R.id.edittext_tasklist_description)
        events.textOnView(R.id.edittext_tasklist_description, taskName)
        events.pressImeActionButton(R.id.edittext_tasklist_description)
        events.waitFor(R.id.recyclerview_tasklist_list, 2000)
        checkThat.listContainsItem(R.id.recyclerview_tasklist_list, taskName)
    }

    private fun addAndOpenTask(taskName: String) {
        addTask(taskName)
        events.clickOnRecyclerItem(R.id.recyclerview_tasklist_list)
        checkThat.viewHasText(R.id.edittext_taskdetail_title, taskName)
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
        checkThat.viewHasDate(R.id.textview_taskdetail_date, calendar)
    }
}
