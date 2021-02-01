package com.escodro.task.presentation.detail

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.escodro.task.model.Category
import com.escodro.task.model.Task
import com.escodro.task.model.TaskWithCategory
import com.escodro.theme.AlkaaTheme
import org.koin.androidx.compose.getViewModel

@Composable
fun TaskDetailSection(taskId: Long) {
    TaskDetailLoader(taskId = taskId)
}

@Composable
private fun TaskDetailLoader(
    taskId: Long,
    viewModel: TaskDetailViewModel = getViewModel()
) {
    viewModel.setTaskInfo(taskId = taskId)
    val state = viewModel.state.collectAsState()

    when (state.value) {
        TaskDetailState.Error -> {/* TODO */
        }
        is TaskDetailState.Loaded -> {
            val data = (state.value as TaskDetailState.Loaded).taskWithCategory
            TaskDetailContent(taskWithCategory = data)
        }
    }
}

@Composable
private fun TaskDetailContent(taskWithCategory: TaskWithCategory) {
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = taskWithCategory.task.title,
        onValueChange = { /*TODO*/ },
        textStyle = MaterialTheme.typography.h3,
        backgroundColor = MaterialTheme.colors.background,
        singleLine = true
    )
}

@Suppress("UndocumentedPublicFunction")
@Preview
@Composable
fun TaskDetailPreview() {
    val task1 = Task(title = "Buy milk", dueDate = null)
    val category1 = Category(name = "Books", color = Color.Green)
    val taskWithCategory = TaskWithCategory(task = task1, category = category1)

    AlkaaTheme {
        TaskDetailContent(taskWithCategory = taskWithCategory)
    }
}
