package com.escodro.task.presentation.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import com.escodro.task.model.Task
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
        TaskDetailState.Error -> {
            /* TODO implement error screen */
        }
        is TaskDetailState.Loaded -> {
            val task = (state.value as TaskDetailState.Loaded).task
            TaskDetailContent(task = task, onTitleChanged = viewModel::updateTitle)
        }
    }
}

@Composable
private fun TaskDetailContent(
    task: Task,
    onTitleChanged: (String) -> Unit
) {
    Column {
        TaskTitleTextField(text = task.title, onTitleChanged = onTitleChanged)
    }
}

@Composable
private fun TaskTitleTextField(
    text: String,
    onTitleChanged: (String) -> Unit
) {
    val textState = remember(key1 = text) { mutableStateOf(TextFieldValue(text)) }
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = textState.value,
        onValueChange = {
            onTitleChanged(it.text)
            textState.value = it
        },
        textStyle = MaterialTheme.typography.h4,
        backgroundColor = MaterialTheme.colors.background,
    )
}

@Suppress("UndocumentedPublicFunction")
@Preview
@Composable
fun TaskDetailPreview() {
    val task = Task(title = "Buy milk", dueDate = null)

    AlkaaTheme {
        TaskDetailContent(task = task) {}
    }
}
