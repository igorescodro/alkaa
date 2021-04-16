package com.escodro.task.presentation.list

import com.escodro.categoryapi.presentation.CategoryState
import com.escodro.task.presentation.detail.main.CategoryId

internal data class CategoryStateHandler(
    val state: CategoryState = CategoryState.Empty,
    val currentCategory: CategoryId? = null,
    val onCategoryChange: (CategoryId?) -> Unit = {},
)
