package com.escodro.task.presentation.fake

import androidx.compose.ui.graphics.Color
import com.escodro.categoryapi.model.Category as ViewCategory
import com.escodro.domain.model.Category as DomainCategory
import com.escodro.domain.model.Task as DomainTask
import com.escodro.domain.model.TaskWithCategory as DomainTaskWithCategory
import com.escodro.task.model.Task as ViewTask
import com.escodro.task.model.TaskWithCategory as ViewTaskWithCategory

val FAKE_VIEW_TASK = ViewTask(id = 43, title = "Buy milk", dueDate = null)

val FAKE_VIEW_CATEGORY = ViewCategory(name = "Books", color = Color.Gray)

val FAKE_VIEW_TASK_WITH_CATEGORY =
    ViewTaskWithCategory(task = FAKE_VIEW_TASK, category = FAKE_VIEW_CATEGORY)

val FAKE_DOMAIN_TASK = DomainTask(id = 43, title = "Buy milk", dueDate = null)

val FAKE_DOMAIN_CATEGORY = DomainCategory(name = "Books", color = "#444444")

val FAKE_DOMAIN_TASK_WITH_CATEGORY =
    DomainTaskWithCategory(task = FAKE_DOMAIN_TASK, category = FAKE_DOMAIN_CATEGORY)

val FAKE_DOMAIN_CATEGORY_LIST = listOf(
    FAKE_DOMAIN_CATEGORY,
    FAKE_DOMAIN_CATEGORY.copy(name = "Movies"),
    FAKE_DOMAIN_CATEGORY.copy(name = "Groceries")
)
