package com.escodro.task.presentation.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import com.escodro.task.R
import com.escodro.task.model.Task
import com.escodro.theme.AlkaaTheme
import com.escodro.theme.components.DefaultIconTextContent
import org.koin.androidx.compose.getViewModel

/**
 * Alkaa Task Detail Section.
 *
 * @param taskId the id from the task to be shown
 */
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
        TaskDetailState.Error -> TaskDetailError()
        is TaskDetailState.Loaded -> {
            val task = (state.value as TaskDetailState.Loaded).task
            TaskDetailContent(
                task = task,
                onTitleChanged = viewModel::updateTitle,
                onDescriptionChanged = viewModel::updateDescription
            )
        }
    }
}

@Composable
private fun TaskDetailContent(
    task: Task,
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit
) {
    Column {
        TaskTitleTextField(text = task.title, onTitleChanged = onTitleChanged)
        TaskDescriptionTextField(
            text = task.description,
            onDescriptionChanged = onDescriptionChanged
        )
    }
}

@Composable
private fun TaskDetailError() {
    DefaultIconTextContent(
        icon = Icons.Outlined.Close,
        iconContentDescription = R.string.task_detail_header_error,
        header = R.string.task_detail_content_description_error
    )
}

@Composable
private fun TaskTitleTextField(text: String, onTitleChanged: (String) -> Unit) {
    val textState = remember { mutableStateOf(TextFieldValue(text)) }

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

@Composable
private fun TaskDescriptionTextField(text: String?, onDescriptionChanged: (String) -> Unit) {
    val textState = remember { mutableStateOf(TextFieldValue(text ?: "")) }

    TextField(
        modifier = Modifier.fillMaxWidth(),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "menu"
            )
        },
        value = textState.value,
        onValueChange = {
            onDescriptionChanged(it.text)
            textState.value = it
        },
        textStyle = MaterialTheme.typography.body1,
        backgroundColor = MaterialTheme.colors.background,
    )
}

@Suppress("UndocumentedPublicFunction")
@Preview
@Composable
fun TaskDetailPreview() {
    val task = Task(title = "Buy milk", description = "This is a amazing task!", dueDate = null)

    AlkaaTheme {
        TaskDetailContent(task = task, {}, {})
    }
}
