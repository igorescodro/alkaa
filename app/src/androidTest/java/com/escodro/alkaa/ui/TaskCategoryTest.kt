package com.escodro.alkaa.ui

import com.escodro.alkaa.R
import com.escodro.alkaa.framework.AcceptanceTest
import com.escodro.alkaa.presentation.MainActivity
import com.escodro.local.model.Category
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Test

/**
 * Test class to validate the Task and Category screens flow.
 */
class TaskCategoryTest : AcceptanceTest<MainActivity>(MainActivity::class.java) {

    @After
    fun cleanTable() = runBlocking {
        daoProvider.getTaskDao().cleanTable()
        daoProvider.getCategoryDao().cleanTable()
    }

    @Test
    fun addTaskAfterRemovingCategoryAndReturningToScreen() = runBlocking {
        val categoryName = "Temporary"
        val taskName = "Should be deleted together"
        val category = Category(name = categoryName, color = "#dd55ff")
        daoProvider.getCategoryDao().insertCategory(category)
        navigateToCategory(categoryName)
        addTask(taskName)
        navigateToCategoryScreen()
        removeCategory(categoryName)
        events.navigateUp()
        checkThat.listNotContainsItem(R.id.recyclerview_tasklist_list, taskName)
        checkThat.viewHasText(R.id.toolbar_title, R.string.task_list_default_title)
        addTask("This is a new task in a new list")
    }

    private fun addTask(taskName: String) {
        events.clickOnView(R.id.edittext_itemadd_description)
        events.textOnView(R.id.edittext_itemadd_description, taskName)
        events.pressImeActionButton(R.id.edittext_itemadd_description)
        events.waitFor(1000)
        checkThat.listContainsItem(R.id.recyclerview_tasklist_list, taskName)
    }

    private fun removeCategory(categoryName: String) {
        events.clickOnView(R.id.imageview_itemcategory_options)
        events.clickOnViewWithText(R.string.category_list_menu_remove)
        events.clickOnViewWithText(R.string.category_list_dialog_remove_positive)
        checkThat.listNotContainsItem(R.id.recyclerview_categorylist_list, categoryName)
        checkThat.viewIsCompletelyDisplayed(R.id.textview_categorylist_empty)
    }

    private fun openDrawer() {
        events.openDrawer(R.id.drawer_layout_main_parent)
        checkThat.drawerIsOpen(R.id.drawer_layout_main_parent)
    }

    private fun navigateToCategoryScreen() {
        openDrawer()
        events.clickOnViewWithText(R.string.drawer_menu_manage_categories)
        checkThat.viewHasText(R.id.toolbar_title, R.string.category_list_label)
    }

    private fun navigateToCategory(categoryName: String) {
        openDrawer()
        events.clickOnViewWithText(categoryName)
        checkThat.viewHasText(R.id.toolbar_title, categoryName)
    }
}
