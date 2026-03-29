package com.escodro.category.fake

import com.escodro.domain.model.TaskGroup
import com.escodro.domain.usecase.taskwithcategory.LoadCategoryTasks
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

internal class LoadCategoryTasksFake : LoadCategoryTasks {
    private val flow = MutableStateFlow<List<TaskGroup>>(emptyList())

    fun emit(groups: List<TaskGroup>) {
        flow.value = groups
    }

    override fun invoke(categoryId: Long): Flow<List<TaskGroup>> = flow

    fun clear() {
        flow.value = emptyList()
    }
}
