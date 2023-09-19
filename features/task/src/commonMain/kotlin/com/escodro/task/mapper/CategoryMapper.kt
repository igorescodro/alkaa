package com.escodro.task.mapper

import com.escodro.designsystem.extensions.toArgbColor
import com.escodro.categoryapi.model.Category as ViewCategory
import com.escodro.domain.model.Category as DomainCategory

internal class CategoryMapper {

    fun toView(domainCategory: DomainCategory): ViewCategory =
        ViewCategory(
            id = domainCategory.id,
            name = domainCategory.name,
            color = domainCategory.color.toArgbColor(),
        )
}
