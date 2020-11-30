package com.escodro.task.mapper

import androidx.compose.ui.graphics.Color
import com.escodro.domain.model.Category as DomainCategory
import com.escodro.task.model.Category as ViewCategory

/**
 * Maps Category between View and Domain.
 */
internal class CategoryMapper {

    /**
     * Maps Category from Domain to View.
     *
     * @param domainCategoryList the list of Category to be converted.
     *
     * @return the converted list of Category
     */
    fun toView(domainCategoryList: List<DomainCategory>): List<ViewCategory> =
        domainCategoryList.map { toView(it) }

    /**
     * Maps Category from Domain to View.
     *
     * @param domainCategory the Category to be converted.
     *
     * @return the converted Category
     */
    fun toView(domainCategory: DomainCategory): ViewCategory =
        ViewCategory(
            id = domainCategory.id,
            name = domainCategory.name,
            color = Color(android.graphics.Color.parseColor(domainCategory.color))
        )
}
