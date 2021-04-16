package com.escodro.category.fake

import com.escodro.domain.model.Category
import com.escodro.domain.usecase.category.LoadAllCategories
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal class LoadAllCategoriesFake : LoadAllCategories {

    var categoriesToBeReturned: List<Category> = listOf()

    override fun invoke(): Flow<List<Category>> =
        flowOf(categoriesToBeReturned)
}
