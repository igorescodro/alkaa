package com.escodro.task.presentation.fake

import com.escodro.domain.model.Category
import com.escodro.domain.usecase.category.LoadAllCategories

internal class LoadAllCategoriesFake : LoadAllCategories {

    var categoriesToBeReturned: List<Category> = listOf()

    override suspend fun invoke(): List<Category> =
        categoriesToBeReturned
}
