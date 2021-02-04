package com.escodro.task.presentation.list.fake

import androidx.compose.ui.graphics.Color
import com.escodro.task.model.Category
import com.escodro.task.model.Task
import com.escodro.task.model.TaskWithCategory

val FAKE_TASK = Task(id = 43, title = "Buy milk", dueDate = null)

val FAKE_CATEGORY = Category(name = "Books", color = Color.Gray)

val FAKE_TASK_WITH_CATEGORY = TaskWithCategory(task = FAKE_TASK, category = FAKE_CATEGORY)
