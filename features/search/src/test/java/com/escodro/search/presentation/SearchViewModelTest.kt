package com.escodro.search.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.escodro.domain.usecase.search.SearchTasksByName
import com.escodro.search.mapper.TaskSearchMapper
import com.escodro.search.model.TaskSearch
import com.escodro.test.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

internal class SearchViewModelTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockUseCase = mockk<SearchTasksByName>(relaxed = true)

    private val mockMapper = mockk<TaskSearchMapper>(relaxed = true)

    private val viewModel = SearchViewModel(mockUseCase, mockMapper)

    @Test
    fun `check if loaded state is returned when there are tasks`() {
        coEvery { mockUseCase.invoke(any()) } returns listOf(mockk(), mockk())

        viewModel.findTasksByName("task name")
        assert(viewModel.state.value is SearchUIState.Loaded)
    }

    @Test
    fun `check if empty state is returned when there are no tasks`() {
        coEvery { mockUseCase.invoke(any()) } returns emptyList()

        viewModel.findTasksByName("bla bla")
        assert(viewModel.state.value is SearchUIState.Empty)
    }

    @Test
    fun `check if the tasks are returned when loaded`() {
        val mockList = listOf<TaskSearch>(mockk(), mockk())
        coEvery { mockUseCase.invoke(any()) } returns listOf(mockk(), mockk())
        coEvery { mockMapper.toTaskSearch(any()) } returns mockList

        viewModel.findTasksByName("a")
        val state = viewModel.state.value as SearchUIState.Loaded
        assert(state.taskList == mockList)
    }
}
