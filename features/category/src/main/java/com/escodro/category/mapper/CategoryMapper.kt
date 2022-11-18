package com.escodro.category.mapper

import android.graphics.Color
import com.escodro.core.extension.toStringColor
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import com.escodro.categoryapi.model.Category as ViewCategory
import com.escodro.domain.model.Category as DomainCategory

internal class CategoryMapper {

    fun toView(domainCategoryList: List<DomainCategory>): ImmutableList<ViewCategory> =
        domainCategoryList.map { toView(it) }.toImmutableList()

    fun toView(domainCategory: DomainCategory): ViewCategory =
        ViewCategory(
            id = domainCategory.id,
            name = domainCategory.name,
            color = Color.parseColor(domainCategory.color)
        )

    fun toDomain(viewCategory: ViewCategory): DomainCategory =
        DomainCategory(
            id = viewCategory.id,
            name = viewCategory.name,
            color = viewCategory.color.toStringColor()
        )
}
