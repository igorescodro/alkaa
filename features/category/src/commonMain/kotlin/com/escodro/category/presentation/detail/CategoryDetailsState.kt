package com.escodro.category.presentation.detail

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.escodro.categoryapi.model.Category
import com.escodro.domain.model.TaskGroup
import kotlinx.collections.immutable.ImmutableList

sealed class CategoryDetailsState {
    data object Loading : CategoryDetailsState()

    data class Error(val throwable: Throwable) : CategoryDetailsState()

    data class Success(val data: CategoryDetailsData) : CategoryDetailsState()
}

@Immutable
data class CategoryDetailsData(
    val category: Category,
    val categoryColor: Color,
    val groups: ImmutableList<TaskGroup>,
    val totalTasks: Int,
    val completedTasks: Int,
)
