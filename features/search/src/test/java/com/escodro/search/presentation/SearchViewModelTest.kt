package com.escodro.search.presentation

import com.escodro.search.fake.SearchTaskByNameFake
import com.escodro.search.mapper.TaskSearchMapper
import org.junit.Assert
import org.junit.Test

internal class SearchViewModelTest {

    private val searchTasksByName = SearchTaskByNameFake()

    private val mockMapper = TaskSearchMapper()

    private val viewModel = SearchViewModel(searchTasksByName, mockMapper)

    @Test
    fun `check if loaded state is returned when there are tasks`() {
        // Given the viewModel is called to search tasks
        val numberOfValues = 10
        searchTasksByName.returnValues(numberOfValues)
        viewModel.findTasksByName("task name")

        // When the latest event is collected
        val state = viewModel.state.value

        // Then the state contain the queried task
        require(state is SearchViewState.Loaded)
        Assert.assertEquals(numberOfValues, state.taskList.size)
    }

    @Test
    fun `check if empty state is returned when there are no tasks`() {
        // Given the viewModel is called to search tasks but don't match the query
        searchTasksByName.returnValues(0)
        viewModel.findTasksByName("bla bla")

        // When the latest event is collected
        val state = viewModel.state.value

        // Then the state is empty
        assert(viewModel.state.value is SearchViewState.Empty)
    }
}
