package com.escodro.task.presentation.list

import com.escodro.task.mapper.AlarmIntervalMapper
import com.escodro.task.mapper.CategoryMapper
import com.escodro.task.mapper.TaskMapper
import com.escodro.task.mapper.TaskWithCategoryMapper
import com.escodro.task.presentation.list.fake.LoadUncompletedTasksFake
import com.escodro.task.presentation.list.fake.UpdateTaskStatusFake
import org.junit.Before
import org.junit.Test

internal class TaskListViewModelTest {

    private val loadUncompletedTasks = LoadUncompletedTasksFake()

    private val updateTaskStatus = UpdateTaskStatusFake()

    private val viewModel = TaskListViewModel(
        loadUncompletedTasks,
        updateTaskStatus,
        TaskWithCategoryMapper(TaskMapper(AlarmIntervalMapper()), CategoryMapper())
    )

    @Before
    fun setup() {
        loadUncompletedTasks.clean()
    }

    @Test
    fun `basic test`() {
        viewModel.loadTasks()
    }
}
