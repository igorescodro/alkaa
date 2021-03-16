package com.escodro.categoryapi.mapper

import com.escodro.domain.model.Category
import com.escodro.categoryapi.model.Category as ViewCategory
import com.escodro.domain.model.Category as DomainCategory

/**
 * Maps Category between View and Domain.
 */
interface CategoryMapper {

    /**
     * Maps Category from Domain to View.
     *
     * @param domainCategoryList the list of Category to be converted.
     *
     * @return the converted list of Category
     */
    fun toView(domainCategoryList: List<Category>): List<ViewCategory>

    /**
     * Maps Category from Domain to View.
     *
     * @param domainCategory the Category to be converted.
     *
     * @return the converted Category
     */
    fun toView(domainCategory: Category): ViewCategory

    /**
     * Maps Category from View to Domain.
     *
     * @param viewCategory the Category to be converted.
     *
     * @return the converted Category
     */
    fun toDomain(viewCategory: ViewCategory): DomainCategory
}
