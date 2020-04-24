package com.escodro.alkaa.ui

import com.escodro.alkaa.R
import com.escodro.alkaa.framework.AcceptanceTest
import com.escodro.alkaa.presentation.MainActivity
import com.escodro.local.model.Task
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class SearchFlowTest : AcceptanceTest<MainActivity>(MainActivity::class.java) {

    @Before
    fun setup() = runBlocking {
        daoProvider.getTaskDao().cleanTable()
        navigateToCategoryScreen()
    }

    @Test
    fun checkIfSearchIsShowingFilteredCriteria() = runBlocking {
        val title = "searching for my heart"
        daoProvider.getTaskDao().insertTask(Task(title = title))
        events.textOnView(R.id.edittext_search_query, "heart")
        events.waitFor(500)
        checkThat.listContainsItem(R.id.recyclerview_search, title)
    }

    @Test
    fun checkIfSearchIsHidingFilteredCriteria() = runBlocking {
        val title = "josie jo jo jo"
        daoProvider.getTaskDao().insertTask(Task(title = title))
        events.textOnView(R.id.edittext_search_query, "heart")
        events.waitFor(500)
        checkThat.listNotContainsItem(R.id.recyclerview_search, title)
    }

    private fun navigateToCategoryScreen() {
        events.clickOnView(R.id.key_action_open_search)
        checkThat.viewHasText(R.id.toolbar_title, R.string.search_title)
    }
}
