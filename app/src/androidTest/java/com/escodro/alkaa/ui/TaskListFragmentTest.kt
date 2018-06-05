package com.escodro.alkaa.ui

import com.escodro.alkaa.R
import com.escodro.alkaa.di.DaoRepository
import com.escodro.alkaa.framework.AcceptanceTest
import org.junit.After
import org.junit.Test
import org.koin.standalone.inject

/**
 * Test class to validate [com.escodro.alkaa.ui.task.TaskListFragment].
 */
class TaskListFragmentTest : AcceptanceTest<MainActivity>(MainActivity::class.java) {

    private val daoRepository: DaoRepository by inject()

    @After
    fun before() {
        daoRepository.getTaskDao().cleanTable()
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
        checkThat.recyclerViewNotContainsItem(R.id.recyclerview_tasklist_list, taskName)
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

    private fun addTask(taskName: String) {
        events.clickOnView(R.id.edittext_tasklist_description)
        events.textOnView(R.id.edittext_tasklist_description, taskName)
        events.pressImeActionButton(R.id.edittext_tasklist_description)
        events.waitFor(R.id.recyclerview_tasklist_list, 1000)
        checkThat.recyclerViewContainsItem(R.id.recyclerview_tasklist_list, taskName)
    }
}
