package com.escodro.category.presentation.detail

import androidx.compose.ui.graphics.Color
import com.escodro.category.mapper.CategoryMapper
import com.escodro.domain.model.Task
import com.escodro.domain.model.TaskGroup
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.LocalDateTime
import com.escodro.domain.model.Category as DomainCategory

internal class CategoryDetailsMapper(
    private val categoryMapper: CategoryMapper,
) {

    fun toViewState(
        domainCategory: DomainCategory,
        groups: List<TaskGroup>,
    ): CategoryDetailsState.Success {
        val viewCategory = categoryMapper.toView(domainCategory)
        val allTasks = groups.flatMap { it.tasks }
        return CategoryDetailsState.Success(
            category = viewCategory,
            categoryColor = Color(viewCategory.color),
            groups = groups.toImmutableList(),
            totalTasks = allTasks.size,
            completedTasks = groups.filterIsInstance<TaskGroup.Completed>()
                .sumOf { it.tasks.size },
        )
    }

    fun toTask(
        title: String,
        dueDate: LocalDateTime?,
        categoryId: Long,
    ): Task = Task(
        title = title,
        dueDate = dueDate,
        categoryId = categoryId,
    )
}
