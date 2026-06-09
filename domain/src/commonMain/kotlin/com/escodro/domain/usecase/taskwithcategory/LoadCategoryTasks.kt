package com.escodro.domain.usecase.taskwithcategory

import com.escodro.domain.model.TaskGroup
import kotlinx.coroutines.flow.Flow

interface LoadCategoryTasks {
    operator fun invoke(categoryId: Long): Flow<List<TaskGroup>>
}
