package com.escodro.alkaa.ui

import com.escodro.alkaa.R
import com.escodro.alkaa.framework.AcceptanceTest
import com.escodro.alkaa.ui.main.MainActivity
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.Calendar

/**
 * Test class to validate Task details.
 */
class TaskDetailFragmentTest : AcceptanceTest<MainActivity>(MainActivity::class.java) {

    @Before
    fun addTasks() {
        addTaskAndOpenTask("Register to vote")
    }

    @After
    fun cleanTable() {
        daoRepository.getTaskDao().cleanTable()
    }

    @Test
    fun setDueDate() {
        val calendar = Calendar.getInstance()
        calendar.set(2018, 9, 23, 6, 30, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        events.clickOnView(R.id.btn_taskdetail_date)
        events.setDate(calendar)
        events.clickOnView(android.R.id.button1)
        events.setTime(calendar)
        events.clickOnView(android.R.id.button1)
        checkThat.viewHasDate(R.id.textview_taskdetail_date, calendar)
    }

    private fun addTaskAndOpenTask(taskName: String) {
        events.clickOnView(R.id.edittext_tasklist_description)
        events.textOnView(R.id.edittext_tasklist_description, taskName)
        events.pressImeActionButton(R.id.edittext_tasklist_description)
        events.waitFor(R.id.recyclerview_tasklist_list, 2000)
        events.clickOnRecyclerItem(R.id.recyclerview_tasklist_list)
    }
}
