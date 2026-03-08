package com.escodro.domain.usecase.taskwithcategory

import com.escodro.domain.model.TaskWithCategory
import kotlinx.coroutines.flow.Flow

interface LoadTasksByCategory {

    operator fun invoke(categoryId: Long): Flow<List<TaskWithCategory>>
}
