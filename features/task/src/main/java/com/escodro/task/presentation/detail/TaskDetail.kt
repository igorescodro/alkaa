package com.escodro.task.presentation.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import com.escodro.task.R
import com.escodro.task.model.Category
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
    val viewState by viewModel.state.collectAsState()

    TaskDetailRouter(
        viewState = viewState,
        onTitleChanged = viewModel::updateTitle,
        onDescriptionChanged = viewModel::updateDescription,
        onCategoryChanged = { } // TODO update with new ViewModel
    )
}

@Composable
internal fun TaskDetailRouter(
    viewState: TaskDetailState,
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onCategoryChanged: (Long?) -> Unit
) {
    when (viewState) {
        TaskDetailState.Error -> TaskDetailError()
        is TaskDetailState.Loaded ->
            TaskDetailContent(
                task = viewState.task,
                categories = listOf(), // TODO update with new ViewModel
                onTitleChanged = onTitleChanged,
                onDescriptionChanged = onDescriptionChanged,
                onCategoryChanged = onCategoryChanged
            )
    }
}

@Composable
private fun TaskDetailContent(
    task: Task,
    categories: List<Category>,
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onCategoryChanged: (Long?) -> Unit
) {
    Surface(color = MaterialTheme.colors.background) {
        Column {
            TaskTitleTextField(text = task.title, onTitleChanged = onTitleChanged)
            CategorySelection(
                categories = categories,
                currentCategory = task.categoryId,
                onCategoryChanged = onCategoryChanged
            )
            TaskDescriptionTextField(
                text = task.description,
                onDescriptionChanged = onDescriptionChanged
            )
        }
    }
}

@Composable
private fun TaskDetailError() {
    DefaultIconTextContent(
        icon = Icons.Outlined.Close,
        iconContentDescription = R.string.task_detail_cd_error,
        header = R.string.task_detail_header_error
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
            LeadingIcon(
                imageVector = Icons.Default.Menu,
                contentDescription = R.string.task_detail_cd_icon_description
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
    val category1 = Category(name = "Groceries", color = Color.Magenta)
    val category2 = Category(name = "Books", color = Color.Cyan)
    val category3 = Category(name = "Movies", color = Color.Red)

    val categories = listOf(category1, category2, category3)

    AlkaaTheme {
        TaskDetailContent(
            task = task,
            categories = categories,
            onTitleChanged = {},
            onDescriptionChanged = {},
            onCategoryChanged = {}
        )
    }
}
