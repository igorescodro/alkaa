package com.escodro.category.fake

import com.escodro.domain.model.Category
import com.escodro.domain.usecase.category.LoadCategory

internal class LoadCategoryFake : LoadCategory {

    var categoryToBeReturned: Category? = null

    override suspend fun invoke(categoryId: Long): Category? =
        categoryToBeReturned
}
