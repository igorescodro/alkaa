package com.escodro.domain.mapper

import com.escodro.domain.viewdata.ViewData
import com.escodro.model.Category

/**
 * Converts between the [Category] model from the database and [ViewData.Category] UI object.
 */
class CategoryMapper {

    /**
     * Maps from a [List] of [Category] to a [List] of [ViewData.Category].
     *
     * @param categoryList list to be mapped
     *
     * @return the converted list
     */
    fun toViewCategory(categoryList: List<Category>) =
        categoryList.map { category ->
            ViewData.Category(category.id, category.name, category.color)
        }

    /**
     * Maps from a [Category] to [ViewData.Category].
     *
     * @param category object to be mapped
     *
     * @return the converted object
     */
    fun toViewCategory(category: Category) =
        ViewData.Category(category.id, category.name, category.color)

    /**
     * Maps from a of [ViewData.Category] to [Category].
     *
     * @param category object to be mapped
     *
     * @return the converted object*
     */
    fun toEntityCategory(category: ViewData.Category) =
        Category(category.id, category.name, category.color)
}
