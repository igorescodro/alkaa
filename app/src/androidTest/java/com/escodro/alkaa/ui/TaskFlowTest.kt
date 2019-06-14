package com.escodro.alkaa.ui

import androidx.test.uiautomator.UiSelector
import com.escodro.alkaa.R
import com.escodro.alkaa.framework.AcceptanceTest
import com.escodro.alkaa.framework.extension.waitForLauncher
import com.escodro.alkaa.ui.main.MainActivity
import com.escodro.model.Category
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
            .blockingGet()
        daoProvider.getCategoryDao().insertCategory(Category(name = "Music", color = "#58a4b0"))
            .blockingGet()
        daoProvider.getCategoryDao().insertCategory(Category(name = "Shared", color = "#519872"))
            .blockingGet()
    }

    @After
    fun cleanTable() {
        daoProvider.getTaskDao().cleanTable().blockingGet()
        daoProvider.getCategoryDao().cleanTable().blockingGet()
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
        checkThat.viewIsCompletelyDisplayed(R.id.chipgrp_taskdetail_category)
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
        events.waitFor(1000)
        checkThat.listNotContainsItem(R.id.recyclerview_tasklist_list, taskName)
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
        events.waitFor(500)
        events.navigateUp()
        events.waitFor(1000)
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
        events.waitFor(500)
        events.navigateUp()
        events.waitFor(1000)
        checkThat.listContainsItem(R.id.recyclerview_tasklist_list, task)
    }

    @Test
    fun checkIfTaskCategoryIsSingleSelection() {
        addAndOpenTask("you'll never walk alone")
        events.clickOnChild(R.id.chipgrp_taskdetail_category, 0)
        events.clickOnChild(R.id.chipgrp_taskdetail_category, 1)
        events.clickOnChild(R.id.chipgrp_taskdetail_category, 2)
        checkThat.viewIsChecked(R.id.chipgrp_taskdetail_category, 2)
        checkThat.viewIsNotChecked(R.id.chipgrp_taskdetail_category, 0)
        checkThat.viewIsNotChecked(R.id.chipgrp_taskdetail_category, 1)
    }

    @Test
    fun checkIfTaskCategoryIsSaved() {
        addAndOpenTask("call my by your name")
        events.clickOnChild(R.id.chipgrp_taskdetail_category, 1)
        events.navigateUp()
        events.clickOnRecyclerItem(R.id.recyclerview_tasklist_list)
        events.waitFor(1000)
        checkThat.viewIsChecked(R.id.chipgrp_taskdetail_category, 1)
    }

    @Test
    fun checkIfTaskCategoryIsRemoved() {
        addAndOpenTask("enigma variations")
        events.clickOnChild(R.id.chipgrp_taskdetail_category, 2)
        events.navigateUp()
        events.clickOnRecyclerItem(R.id.recyclerview_tasklist_list)
        events.waitFor(1000)
        events.clickOnChild(R.id.chipgrp_taskdetail_category, 2)
        events.navigateUp()
        events.clickOnRecyclerItem(R.id.recyclerview_tasklist_list)
        checkThat.viewIsNotChecked(R.id.chipgrp_taskdetail_category, 1)
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
        events.waitFor(500)
        events.navigateUp()
        events.waitFor(2000)
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
        uiDevice.waitForLauncher()
        uiDevice.findObject(UiSelector().descriptionContains(context.getString(R.string.app_name)))
            .click()

        addTask("It must reflect on UI")
    }

    @Test
    fun validateCategoryTitlePressingBack() {
        val title = "What lovers do"
        addAndOpenTask(title)
        uiDevice.pressBack()
        checkThat.viewHasText(R.id.toolbar_title, R.string.drawer_menu_all_tasks)
    }

    @Test
    fun validateCategoryTitlePressingUp() {
        val title = "UH UH UH UH UH"
        addAndOpenTask(title)
        events.navigateUp()
        checkThat.viewHasText(R.id.toolbar_title, R.string.drawer_menu_all_tasks)
    }

    @Test
    fun validateLongDescription() {
        val title = "La vie en rose"
        addAndOpenTask(title)

        val description = StringBuilder()
        for (i in 0..50) {
            description.append("Quand il me prend dans ses bras [$i]\n")
        }

        events.textOnView(R.id.edittext_taskdetail_description, description.toString())
        events.waitFor(500)
        checkThat.viewIsNotDisplayed(R.id.btn_taskdetail_date)
        events.scrollTo(R.id.btn_taskdetail_date)
        checkThat.viewIsCompletelyDisplayed(R.id.btn_taskdetail_date)
    }

    @Test
    fun checkIfSecondTaskHasCleanCategory() {
        val title = "Thanks a lot for your help. :)"
        addAndOpenTask("I really want this... Help me?")
        events.clickOnChild(R.id.chipgrp_taskdetail_category, 0)
        events.navigateUp()
        addAndOpenTask(title)
        checkThat.viewIsNotChecked(R.id.chipgrp_taskdetail_category, 0)
    }

    private fun addTask(taskName: String) {
        events.clickOnView(R.id.edittext_itemadd_description)
        events.textOnView(R.id.edittext_itemadd_description, taskName)
        events.pressImeActionButton(R.id.edittext_itemadd_description)
        events.waitFor(1000)
        checkThat.listContainsItem(R.id.recyclerview_tasklist_list, taskName)
    }

    private fun addAndOpenTask(taskName: String) {
        addTask(taskName)
        events.clickOnViewWithText(taskName)
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
