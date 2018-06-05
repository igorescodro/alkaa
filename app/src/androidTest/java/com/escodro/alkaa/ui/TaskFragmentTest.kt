package com.escodro.alkaa.ui

import com.escodro.alkaa.R
import com.escodro.alkaa.di.DaoRepository
import com.escodro.alkaa.framework.AcceptanceTest
import org.junit.After
import org.junit.Test
import org.koin.standalone.inject

/**
 * Test class to validate [com.escodro.alkaa.ui.task.TaskFragment].
 */
class TaskFragmentTest : AcceptanceTest<MainActivity>(MainActivity::class.java) {

    private val daoRepository: DaoRepository by inject()

    @After
    fun before() {
        daoRepository.getTaskDao().cleanTable()
    }

    @Test
    fun areAllViewsIsCompletelyDisplayed() {
        checkThat.viewIsCompletelyDisplayed(R.id.edit_text)
        checkThat.viewIsCompletelyDisplayed(R.id.recycler_view)
    }

    @Test
    fun isDescriptionSingleLine() {
        addTask(
            "Lorem ipsum dolor sit amet, te elit possit suavitate duo. Nec sale sonet" +
                    " scriptorem ei, option prompta ut sed. At everti discere oportere sea."
        )
        checkThat.textHasFixedLines(R.id.task_description, 1)
    }

    @Test
    fun addNewTask() {
        addTask("buy milk")
    }

    @Test
    fun deleteTask() {
        val taskName = "eat vegetables"
        addTask(taskName)
        events.longPressOnRecyclerItem(R.id.recycler_view)
        events.clickDialogOption(R.array.task_dialog_options, 0)
        checkThat.recyclerViewNotContainsItem(R.id.recycler_view, taskName)
    }

    @Test
    fun addEmptyTask() {
        events.clickOnView(R.id.edit_text)
        events.pressImeActionButton(R.id.edit_text)
        checkThat.viewContainsError(R.id.edit_text, R.string.task_error_empty)
    }

    @Test
    fun checkTaskAsCompleted() {
        addTask("write article")
        events.clickOnView(R.id.checkbox)
        checkThat.checkBoxIsChecked(R.id.checkbox)
    }

    private fun addTask(taskName: String) {
        events.clickOnView(R.id.edit_text)
        events.textOnView(R.id.edit_text, taskName)
        events.pressImeActionButton(R.id.edit_text)
        events.waitFor(R.id.recycler_view, 1000)
        checkThat.recyclerViewContainsItem(R.id.recycler_view, taskName)
    }
}
