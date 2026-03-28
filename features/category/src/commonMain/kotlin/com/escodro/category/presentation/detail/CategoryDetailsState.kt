package com.escodro.category.presentation.detail

import androidx.compose.ui.graphics.Color
import com.escodro.categoryapi.model.Category
import com.escodro.domain.model.TaskGroup

sealed class CategoryDetailsState {
    data object Loading : CategoryDetailsState()
    data class Error(val throwable: Throwable) : CategoryDetailsState()
    data class Success(
        val category: Category,
        val categoryColor: Color,
        val groups: List<TaskGroup>,
        val totalTasks: Int,
        val completedTasks: Int,
    ) : CategoryDetailsState()
}
