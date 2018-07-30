package com.escodro.alkaa.ui

import com.escodro.alkaa.R
import com.escodro.alkaa.data.local.model.Category
import com.escodro.alkaa.framework.AcceptanceTest
import com.escodro.alkaa.ui.main.MainActivity
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Test class to validate Task screen and flow.
 */
class TaskListFragmentTest : AcceptanceTest<MainActivity>(MainActivity::class.java) {

    @Before
    fun addCategories() {
        daoRepository.getCategoryDao().insertCategory(Category("Work", "#cc5a71"))
        daoRepository.getCategoryDao().insertCategory(Category("Personal", "#58a4b0"))
        daoRepository.getCategoryDao().insertCategory(Category("Family", "#519872"))
    }

    @After
    fun cleanTable() {
        daoRepository.getTaskDao().cleanTable()
        daoRepository.getCategoryDao().cleanTable()
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
    fun checkIfTaskCategoryIsSaved() {
        addAndOpenTask("call my by your name")
        events.clickOnRadioButton(R.id.srg_radiogroup_list, 1)
        events.navigateUp()
        events.clickOnRecyclerItem(R.id.recyclerview_tasklist_list)
        checkThat.radioButtonIsChecked(R.id.srg_radiogroup_list, 1)
    }

    private fun addAndOpenTask(taskName: String) {
        addTask(taskName)
        events.clickOnRecyclerItem(R.id.recyclerview_tasklist_list)
        checkThat.toolbarContainsTitle(R.id.toolbar_main_toolbar, taskName)
    }

    private fun addTask(taskName: String) {
        events.clickOnView(R.id.edittext_tasklist_description)
        events.textOnView(R.id.edittext_tasklist_description, taskName)
        events.pressImeActionButton(R.id.edittext_tasklist_description)
        events.waitFor(R.id.recyclerview_tasklist_list, 2000)
        checkThat.listContainsItem(R.id.recyclerview_tasklist_list, taskName)
    }
}
