package com.escodro.alkaa.ui

import com.escodro.alkaa.R
import com.escodro.alkaa.framework.AcceptanceTest
import com.escodro.alkaa.presentation.MainActivity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Test

/**
 * Test class to validate Task completed flow.
 */
class CompletedTaskTest : AcceptanceTest<MainActivity>(MainActivity::class.java) {

    @After
    fun cleanTable() = runBlocking {
        daoProvider.getTaskDao().cleanTable()
    }

    @Test
    fun checkTaskAsCompleted() {
        val taskName = "write article"
        addTask(taskName)
        events.clickOnView(R.id.checkbox_itemtask_completed)
        checkThat.listNotContainsItem(R.id.recyclerview_tasklist_list, taskName)
    }

    @Test
    fun validateIfTaskIsCompleted() {
        val title = "when the party's over"
        addTask(title)
        events.clickOnView(R.id.checkbox_itemtask_completed)
        checkThat.listNotContainsItem(R.id.recyclerview_tasklist_list, title)
        openDrawer()
        events.clickOnViewWithText(R.string.drawer_menu_completed_tasks)
        checkThat.viewHasText(R.id.toolbar_title, R.string.drawer_menu_completed_tasks)
        checkThat.listContainsItem(R.id.recyclerview_tasklist_list, title)
    }

    @Test
    fun validateIfSnackbarIsShown() {
        val title = "buy more snacks to Super Bowl"
        addTask(title)
        events.clickOnView(R.id.checkbox_itemtask_completed)
        checkThat.listNotContainsItem(R.id.recyclerview_tasklist_list, title)
        checkThat.snackbarIsVisible()
    }

    @Test
    fun removeCompleteStatusFromTask() {
        val title = "create more playlists"
        addTask(title)
        events.clickOnView(R.id.checkbox_itemtask_completed)
        checkThat.listNotContainsItem(R.id.recyclerview_tasklist_list, title)

        openDrawer()
        events.clickOnViewWithText(R.string.drawer_menu_completed_tasks)
        checkThat.viewHasText(R.id.toolbar_title, R.string.drawer_menu_completed_tasks)
        checkThat.listContainsItem(R.id.recyclerview_tasklist_list, title)
        events.clickOnView(R.id.checkbox_itemtask_completed)
        checkThat.listNotContainsItem(R.id.recyclerview_tasklist_list, title)

        openDrawer()
        events.clickOnViewWithText(R.string.drawer_menu_all_tasks)
        checkThat.viewHasText(R.id.toolbar_title, R.string.drawer_menu_all_tasks)
        checkThat.listContainsItem(R.id.recyclerview_tasklist_list, title)
    }

    @Test
    fun undoCompleteStatusFromTask() {
        val title = "sorry, I made a mistake :D"
        addTask(title)
        events.clickOnView(R.id.checkbox_itemtask_completed)
        checkThat.listNotContainsItem(R.id.recyclerview_tasklist_list, title)
        events.clickOnViewWithText(R.string.task_snackbar_undo)
        checkThat.listContainsItem(R.id.recyclerview_tasklist_list, title)
    }

    @Test
    fun validateIfAddButtonIsVisibleInMainList() {
        checkThat.listContainsHint(R.id.recyclerview_tasklist_list, R.string.task_add_new)
    }

    @Test
    fun validateIfAddButtonIsNotVisibleInCompletedList() {
        openDrawer()
        events.clickOnViewWithText(R.string.drawer_menu_completed_tasks)
        checkThat.viewHasText(R.id.toolbar_title, R.string.drawer_menu_completed_tasks)
        checkThat.listNotContainsHint(R.id.recyclerview_tasklist_list, R.string.task_add_new)
    }

    @Test
    fun checkIfUpdateTaskStatusIsWorking() {
        val taskName = "totoooooooro"
        addTask(taskName)
        events.clickOnViewWithText(taskName)
        events.clickOnView(R.id.key_update_completed_status)
        events.navigateUp()
        checkThat.listNotContainsItem(R.id.recyclerview_tasklist_list, taskName)
    }

    @Test
    fun checkIfRevertingTaskStatusIsWorking() {
        val taskName = "chihiroooooo"
        addTask(taskName)
        events.clickOnViewWithText(taskName)
        events.clickOnView(R.id.key_update_completed_status)
        events.clickOnView(R.id.key_update_completed_status)
        events.navigateUp()
        checkThat.listContainsItem(R.id.recyclerview_tasklist_list, taskName)
    }

    private fun addTask(taskName: String) {
        events.clickOnView(R.id.edittext_itemadd_description)
        events.textOnView(R.id.edittext_itemadd_description, taskName)
        events.pressImeActionButton(R.id.edittext_itemadd_description)
        events.waitFor(1000)
        checkThat.listContainsItem(R.id.recyclerview_tasklist_list, taskName)
    }

    private fun openDrawer() {
        events.openDrawer(R.id.drawer_layout_main_parent)
        checkThat.drawerIsOpen(R.id.drawer_layout_main_parent)
    }
}
