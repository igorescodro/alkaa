package com.escodro.task.presentation.fake

import com.escodro.domain.model.Category
import com.escodro.domain.model.Task
import com.escodro.domain.model.TaskWithCategory
import com.escodro.domain.usecase.taskwithcategory.LoadTasksByCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

internal class LoadTasksByCategoryFake : LoadTasksByCategory {

    private val tasks = MutableStateFlow<List<TaskWithCategory>>(emptyList())
    var isErrorThrown = false

    fun setTasks(list: List<TaskWithCategory>) {
        tasks.value = list
    }

    fun clear() {
        tasks.value = emptyList()
        isErrorThrown = false
    }

    override fun invoke(categoryId: Long): Flow<List<TaskWithCategory>> =
        tasks.map { list ->
            if (isErrorThrown) throw IllegalStateException("Fake error")
            list
        }
}
