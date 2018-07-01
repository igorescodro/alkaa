package com.escodro.alkaa.ui

import com.escodro.alkaa.R
import com.escodro.alkaa.data.local.model.Category
import com.escodro.alkaa.data.local.model.Task
import com.escodro.alkaa.framework.AcceptanceTest
import com.escodro.alkaa.ui.main.MainActivity
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Test class to validate the Main Drawer and flow.
 */
class MainDrawerTest : AcceptanceTest<MainActivity>(MainActivity::class.java) {

    private var categoryPersonalId = 0L
    private var categoryWorkId = 0L
    private var categoryFamilyId = 0L

    @Before
    fun populateApplication() {
        val categoryDao = daoRepository.getCategoryDao()
        val taskDao = daoRepository.getTaskDao()

        categoryDao.insertCategory(Category(PERSONAL_CATEGORY, "#cc5a71"))
        categoryDao.insertCategory(Category(WORK_CATEGORY, "#58a4b0"))
        categoryDao.insertCategory(Category(FAMILY_CATEGORY, "#519872"))

        categoryPersonalId = categoryDao.findTaskByName(PERSONAL_CATEGORY).id
        categoryWorkId = categoryDao.findTaskByName(WORK_CATEGORY).id
        categoryFamilyId = categoryDao.findTaskByName(FAMILY_CATEGORY).id

        taskDao.insertTask(Task(false, "Buy milk", categoryPersonalId))
        taskDao.insertTask(Task(true, "Buy onion", categoryPersonalId))
        taskDao.insertTask(Task(false, "Study presentation", categoryWorkId))
        taskDao.insertTask(Task(false, "Visit grandpa", categoryFamilyId))
        taskDao.insertTask(Task(false, "Call dad", categoryFamilyId))
    }

    @After
    fun cleanTable() {
        daoRepository.getTaskDao().cleanTable()
        daoRepository.getCategoryDao().cleanTable()
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
        events.clickOnNavigationViewItem(R.id.navigationview_main_drawer, categoryFamilyId.toInt())
        events.waitFor(R.id.layout_main_parent, 600)
        checkThat.drawerIsClosed(R.id.layout_main_parent)
        checkThat.viewHasText(R.id.textview_tasklist_category, FAMILY_CATEGORY)
    }

    @Test
    fun checkDrawerFlowFromTaskDetails() {
        checkDrawerMainFlow()
        events.clickOnRecyclerItem(R.id.recyclerview_tasklist_list)
        openDrawer()
        events.clickOnNavigationViewItem(
            R.id.navigationview_main_drawer,
            categoryPersonalId.toInt()
        )
        events.waitFor(R.id.layout_main_parent, 600)
        checkThat.drawerIsClosed(R.id.layout_main_parent)
        checkThat.viewHasText(R.id.textview_tasklist_category, PERSONAL_CATEGORY)
    }

    private fun openDrawer() {
        events.openDrawer(R.id.layout_main_parent)
        checkThat.drawerIsOpen(R.id.layout_main_parent)
    }

    companion object {

        private const val PERSONAL_CATEGORY = "Personal"
        private const val WORK_CATEGORY = "Work"
        private const val FAMILY_CATEGORY = "Family"
    }
}
