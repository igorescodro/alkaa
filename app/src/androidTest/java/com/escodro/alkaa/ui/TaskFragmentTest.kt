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
    fun addNewTask() {
        events.clickOnView(R.id.edit_text)
        events.textOnView(R.id.edit_text, TASK_NAME)
        events.pressImeActionButton(R.id.edit_text)
        events.waitFor(R.id.recycler_view, 1000)
        checkThat.recyclerViewContainsItem(R.id.recycler_view, TASK_NAME)
    }

    @Test
    fun deleteTask() {
        addNewTask()
        events.longPressOnRecyclerItem(R.id.recycler_view)
        events.clickDialogOption(R.array.task_dialog_options, 0)
        checkThat.recyclerViewNotContainsItem(R.id.recycler_view, TASK_NAME)
    }

    @Test
    fun addEmptyTask() {
        events.clickOnView(R.id.edit_text)
        events.pressImeActionButton(R.id.edit_text)
        checkThat.viewContainsError(R.id.edit_text, R.string.task_error_empty)
    }

    @Test
    fun checkTaskAsCompleted() {
        addNewTask()
        events.clickOnView(R.id.checkbox)
    }

    companion object {

        private const val TASK_NAME = "buy milk"
    }
}
