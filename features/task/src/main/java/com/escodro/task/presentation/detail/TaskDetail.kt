package com.escodro.task.presentation.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    val state = viewModel.state.collectAsState()

    when (state.value) {
        TaskDetailState.Error -> TaskDetailError()
        is TaskDetailState.Loaded -> {
            val task = (state.value as TaskDetailState.Loaded).task
            val categories = (state.value as TaskDetailState.Loaded).categoryList
            TaskDetailContent(
                task = task,
                categories = categories,
                onTitleChanged = viewModel::updateTitle,
                onDescriptionChanged = viewModel::updateDescription,
                onCategoryChanged = viewModel::updateCategory
            )
        }
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
        iconContentDescription = R.string.task_detail_header_error,
        header = R.string.task_detail_cd_error
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

@Composable
private fun CategorySelection(
    categories: List<Category>,
    currentCategory: Long?,
    onCategoryChanged: (Long?) -> Unit
) {
    val currentItem = categories.find { category -> category.id == currentCategory }
    val selectedState = remember { mutableStateOf(currentItem) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .preferredSize(56.dp)
            .padding(start = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LeadingIcon(
            imageVector = Icons.Default.Create,
            contentDescription = R.string.task_detail_cd_icon_category
        )
        LazyRow(modifier = Modifier.padding(start = 16.dp)) {
            items(
                items = categories,
                itemContent = { category ->
                    val isSelected = category == selectedState.value
                    CategoryItemChip(
                        category = category,
                        isSelected = isSelected,
                        selectedState,
                        onCategoryChanged = onCategoryChanged
                    )
                }
            )
        }
    }
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
