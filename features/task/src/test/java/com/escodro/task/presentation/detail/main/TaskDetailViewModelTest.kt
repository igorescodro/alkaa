package com.escodro.task.presentation.detail.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.escodro.domain.usecase.task.UpdateTask
import com.escodro.domain.usecase.task.UpdateTaskStatus
import com.escodro.task.mapper.AlarmIntervalMapper
import com.escodro.task.mapper.TaskMapper
import com.escodro.test.CoroutineTestRule
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

internal class TaskDetailViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val fakeProvider = FakeTaskDetailProvider()

    private val mockUpdateTaskUseCase = mockk<UpdateTask>()

    private val mockUpdateStatusUseCase = mockk<UpdateTaskStatus>(relaxed = true)

    private val mapper = TaskMapper(AlarmIntervalMapper())

    private val viewModel =
        TaskDetailViewModel(fakeProvider, mockUpdateTaskUseCase, mockUpdateStatusUseCase, mapper)

    @Test
    fun `check if view state is Completed when the task is completed`() {
        fakeProvider.isTaskCompleted = true
        viewModel.loadTask(id = 1)
        fakeProvider.taskData.observeForever { /* Workaround to enable LiveData stream */ }
        viewModel.state.observeForever { /* Workaround to enable LiveData stream */ }
        Assert.assertEquals(TaskDetailUIState.Completed, viewModel.state.value)
    }

    @Test
    fun `check if view state is Uncompleted when task is not completed`() {
        fakeProvider.isTaskCompleted = false
        viewModel.loadTask(id = 1)
        fakeProvider.taskData.observeForever { /* Workaround to enable LiveData stream */ }
        viewModel.state.observeForever { /* Workaround to enable LiveData stream */ }
        Assert.assertEquals(TaskDetailUIState.Uncompleted, viewModel.state.value)
    }

    @Test
    fun `check if the use case is called to update the task status`() {
        viewModel.loadTask(id = 1)
        viewModel.updateTaskStatus()
        coVerify { mockUpdateStatusUseCase.invoke(fakeProvider.taskData.value!!.id) }
    }
}
