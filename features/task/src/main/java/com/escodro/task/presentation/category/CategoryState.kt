package com.escodro.task.presentation.category

import com.escodro.task.model.Category

internal sealed class CategoryState {

    object Loading : CategoryState()

    data class Loaded(val categoryList: List<Category>) : CategoryState()

    object Empty : CategoryState()
}
