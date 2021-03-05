package com.escodro.task.presentation.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escodro.domain.model.Task
import com.escodro.domain.usecase.task.AddTask
import kotlinx.coroutines.launch

internal class AddTaskViewModel(
    private val addTaskUseCase: AddTask
) : ViewModel() {

    fun addTask(title: String) {
        if (title.isBlank()) return

        viewModelScope.launch {
            val task = Task(title = title)
            addTaskUseCase.invoke(task)
        }
    }
}
