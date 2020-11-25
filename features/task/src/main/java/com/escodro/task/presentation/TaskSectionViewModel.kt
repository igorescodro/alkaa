package com.escodro.task.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escodro.domain.usecase.taskwithcategory.LoadUncompletedTasks
import com.escodro.task.mapper.TaskWithCategoryMapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.core.context.GlobalContext

/**
 * ViewModel responsible to handle the interaction between the presentation and business logic from
 * Task Section.
 */
internal class TaskSectionViewModel : ViewModel() {

    /*
     * TODO Remove this lazy injection when updating to Koin 2.2.1.
     *  This is a workaround due to Koin crashes
     */
    private val koin = GlobalContext.get()
    private val loadAllTasksUseCase: LoadUncompletedTasks = koin.get()
    private val taskWithCategoryMapper: TaskWithCategoryMapper = koin.get()

    private val _state = MutableStateFlow(TaskListViewState(listOf()))

    val state: StateFlow<TaskListViewState>
        get() = _state

    fun loadTasks() = viewModelScope.launch {
        loadAllTasksUseCase()
            .map { task -> taskWithCategoryMapper.toView(task) }
            .collect { tasks ->
                _state.value = TaskListViewState(tasks)
            }
    }
}
