package com.escodro.domain.usecase.taskwithcategory.implementation

import com.escodro.domain.model.TaskWithCategory
import com.escodro.domain.usecase.taskwithcategory.LoadCompletedTasks
import com.escodro.domain.usecase.taskwithcategory.LoadTasksByCategory
import com.escodro.domain.usecase.taskwithcategory.LoadUncompletedTasks
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

internal class LoadTasksByCategoryImpl(
    private val loadUncompletedTasks: LoadUncompletedTasks,
    private val loadCompletedTasks: LoadCompletedTasks,
) : LoadTasksByCategory {

    override fun invoke(categoryId: Long): Flow<List<TaskWithCategory>> =
        combine(
            flow = loadUncompletedTasks(categoryId),
            flow2 = loadCompletedTasks(categoryId),
        ) { uncompleted, completed -> uncompleted + completed }
}
