package com.escodro.alkaa.ui

import com.escodro.alkaa.R
import com.escodro.alkaa.framework.AcceptanceTest
import com.escodro.alkaa.ui.main.MainActivity
import com.escodro.model.Category
import com.escodro.model.Task
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Test class to validate the Main Drawer and flow.
 */
class MainDrawerTest : AcceptanceTest<MainActivity>(MainActivity::class.java) {

    private var personalId = 0L
    private var workId = 0L
    private var familyId = 0L

    @Before
    fun populateApplication() {
        val categoryDao = daoProvider.getCategoryDao()
        val taskDao = daoProvider.getTaskDao()

        categoryDao.insertCategory(Category(name = PERSONAL_CATEGORY, color = "#cc5a71"))
        categoryDao.insertCategory(Category(name = WORK_CATEGORY, color = "#58a4b0"))
        categoryDao.insertCategory(Category(name = FAMILY_CATEGORY, color = "#519872"))

        personalId = categoryDao.findCategoryByName(PERSONAL_CATEGORY).blockingGet().id
        workId = categoryDao.findCategoryByName(WORK_CATEGORY).blockingGet().id
        familyId = categoryDao.findCategoryByName(FAMILY_CATEGORY).blockingGet().id

        taskDao.insertTask(Task(completed = false, title = "Buy milk", categoryId = personalId))
        taskDao.insertTask(Task(completed = true, title = "Buy onion", categoryId = personalId))
        taskDao.insertTask(Task(completed = false, title = "Study docs", categoryId = workId))
        taskDao.insertTask(Task(completed = false, title = "Visit grandpa", categoryId = familyId))
        taskDao.insertTask(Task(completed = false, title = "Call dad", categoryId = familyId))
    }

    @After
    fun cleanTable() {
        daoProvider.getTaskDao().cleanTable()
        daoProvider.getCategoryDao().cleanTable()
    }

    @Test
    fun openAndValidateDrawer() {
        openDrawer()
    }

    @Test
    fun areAllCategoriesAvailable() {
        openDrawer()
        checkThat.listContainsItem(R.id.navigationview_main_drawer, "All Tasks")
        checkThat.listContainsItem(R.id.navigationview_main_drawer, PERSONAL_CATEGORY)
        checkThat.listContainsItem(R.id.navigationview_main_drawer, WORK_CATEGORY)
        checkThat.listContainsItem(R.id.navigationview_main_drawer, FAMILY_CATEGORY)
    }

    @Test
    fun checkDrawerMainFlow() {
        openDrawer()
        events.clickOnNavigationViewItem(R.id.navigationview_main_drawer, familyId.toInt())
        events.waitFor(600)
        checkThat.drawerIsClosed(R.id.drawer_layout_main_parent)
        checkThat.viewHasText(R.id.toolbar_title, FAMILY_CATEGORY)
    }

    @Test
    fun checkDrawerFlowFromTaskDetails() {
        checkDrawerMainFlow()
        events.clickOnRecyclerItem(R.id.recyclerview_tasklist_list)
        openDrawer()
        events.clickOnNavigationViewItem(
            R.id.navigationview_main_drawer,
            personalId.toInt()
        )
        events.waitFor(600)
        checkThat.drawerIsClosed(R.id.drawer_layout_main_parent)
        checkThat.viewHasText(R.id.toolbar_title, PERSONAL_CATEGORY)
    }

    private fun openDrawer() {
        events.openDrawer(R.id.drawer_layout_main_parent)
        checkThat.drawerIsOpen(R.id.drawer_layout_main_parent)
    }

    companion object {

        private const val PERSONAL_CATEGORY = "Personal"
        private const val WORK_CATEGORY = "Work"
        private const val FAMILY_CATEGORY = "Family"
    }
}
