package com.escodro.task.presentation.detail

import com.escodro.task.model.Category

internal sealed class TaskCategoryState {

    data class Loaded(val categoryList: List<Category>) : TaskCategoryState()

    object Empty : TaskCategoryState()
}
