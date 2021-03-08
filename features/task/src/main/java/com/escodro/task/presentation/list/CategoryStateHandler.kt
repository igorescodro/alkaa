package com.escodro.task.presentation.list

import com.escodro.task.presentation.category.CategoryState
import com.escodro.task.presentation.detail.main.CategoryId

internal data class CategoryStateHandler(
    val state: CategoryState = CategoryState.Empty,
    val currentCategory: CategoryId? = null,
    val onCategoryChanged: (CategoryId?) -> Unit = {},
)
