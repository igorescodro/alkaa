package com.escodro.category.mapper

import android.graphics.Color
import com.escodro.categoryapi.mapper.CategoryMapper
import com.escodro.core.extension.toStringColor
import com.escodro.categoryapi.model.Category as ViewCategory
import com.escodro.domain.model.Category as DomainCategory

internal class CategoryMapperImpl : CategoryMapper {

    override fun toView(domainCategoryList: List<DomainCategory>): List<ViewCategory> =
        domainCategoryList.map { toView(it) }

    override fun toView(domainCategory: DomainCategory): ViewCategory =
        ViewCategory(
            id = domainCategory.id,
            name = domainCategory.name,
            color = Color.parseColor(domainCategory.color)
        )

    override fun toDomain(viewCategory: ViewCategory): DomainCategory =
        DomainCategory(
            id = viewCategory.id,
            name = viewCategory.name,
            color = viewCategory.color.toStringColor()
        )
}
