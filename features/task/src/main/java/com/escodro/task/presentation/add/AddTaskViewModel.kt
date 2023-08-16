package com.escodro.task.presentation.add

import androidx.lifecycle.ViewModel
import com.escodro.coroutines.AppCoroutineScope
import com.escodro.domain.model.Task
import com.escodro.domain.usecase.task.AddTask
import com.escodro.task.presentation.detail.main.CategoryId

internal class AddTaskViewModel(
    private val addTaskUseCase: AddTask,
    private val applicationScope: AppCoroutineScope,
) : ViewModel() {

    fun addTask(title: String, categoryId: CategoryId?) {
        if (title.isBlank()) return

        applicationScope.launch {
            val task = Task(title = title, categoryId = categoryId?.value)
            addTaskUseCase.invoke(task)
        }
    }
}
