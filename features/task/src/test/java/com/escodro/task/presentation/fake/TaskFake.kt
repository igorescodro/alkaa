package com.escodro.task.presentation.fake

import com.escodro.categoryapi.model.Category as ViewCategory
import com.escodro.domain.model.Task as DomainTask
import com.escodro.task.model.Task as ViewTask
import com.escodro.task.model.TaskWithCategory as ViewTaskWithCategory

val FAKE_VIEW_TASK = ViewTask(id = 43, title = "Buy milk", dueDate = null)

val FAKE_VIEW_CATEGORY = ViewCategory(name = "Books", color = android.graphics.Color.GRAY)

val FAKE_VIEW_TASK_WITH_CATEGORY =
    ViewTaskWithCategory(task = FAKE_VIEW_TASK, category = FAKE_VIEW_CATEGORY)

val FAKE_DOMAIN_TASK = DomainTask(id = 43, title = "Buy milk", dueDate = null)
