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

    @Before
    fun populateApplication() {
        val categoryDao = daoRepository.getCategoryDao()
        val taskDao = daoRepository.getTaskDao()

        categoryDao.insertCategory(Category("Work", "#cc5a71"))
        categoryDao.insertCategory(Category("Personal", "#58a4b0"))
        categoryDao.insertCategory(Category("Family", "#519872"))

        val categoryWorkId = categoryDao.findTaskByName("Work").id
        val categoryPersonalId = categoryDao.findTaskByName("Personal").id
        val categoryFamilyId = categoryDao.findTaskByName("Family").id

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
        checkThat.listContainsItem(R.id.navigationview_main_drawer, "Work")
        checkThat.listContainsItem(R.id.navigationview_main_drawer, "Personal")
        checkThat.listContainsItem(R.id.navigationview_main_drawer, "Family")
    }

    private fun openDrawer() {
        events.openDrawer(R.id.layout_main_parent)
        checkThat.drawerIsOpen(R.id.layout_main_parent)
    }
}
