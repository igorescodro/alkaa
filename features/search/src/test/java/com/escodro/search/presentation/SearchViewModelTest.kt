package com.escodro.search.presentation

import com.escodro.search.fake.SearchTaskByNameFake
import com.escodro.search.mapper.TaskSearchMapper
import com.escodro.test.CoroutineTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class SearchViewModelTest {

    @get:Rule
    val coroutinesRule = CoroutineTestRule()

    private val searchTasksByName = SearchTaskByNameFake()

    private val mockMapper = TaskSearchMapper()

    private val viewModel = SearchViewModel(searchTasksByName, mockMapper)

    @Test
    fun `check if loaded state is returned when there are tasks`() =
        coroutinesRule.testDispatcher.runBlockingTest {

            // Given the viewModel is called to search tasks
            val numberOfValues = 10
            searchTasksByName.returnValues(numberOfValues)
            val flow = viewModel.findTasksByName("task name")

            // When the latest event is collected
            val state = flow.first()

            // Then the state contain the queried task
            coroutinesRule.testDispatcher.advanceUntilIdle()
            require(state is SearchViewState.Loaded)
            Assert.assertEquals(numberOfValues, state.taskList.size)
        }

    @Test
    fun `check if empty state is returned when there are no tasks`() =
        coroutinesRule.testDispatcher.runBlockingTest {

            // Given the viewModel is called to search tasks but don't match the query
            searchTasksByName.returnValues(0)
            val flow = viewModel.findTasksByName("bla bla")

            // When the latest event is collected
            val state = flow.first()

            // Then the state is empty
            assert(state is SearchViewState.Empty)
        }
}
