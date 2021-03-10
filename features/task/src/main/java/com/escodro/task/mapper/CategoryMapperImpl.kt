package com.escodro.task.mapper

import androidx.compose.ui.graphics.Color
import com.escodro.categoryapi.mapper.CategoryMapper
import com.escodro.categoryapi.model.Category as ViewCategory
import com.escodro.domain.model.Category as DomainCategory

internal class CategoryMapperImpl : CategoryMapper {

    override fun toView(domainCategoryList: List<DomainCategory>): List<ViewCategory> =
        domainCategoryList.map { toView(it) }

    override fun toView(domainCategory: DomainCategory): ViewCategory =
        ViewCategory(
            id = domainCategory.id,
            name = domainCategory.name,
            color = Color(android.graphics.Color.parseColor(domainCategory.color))
        )
}
