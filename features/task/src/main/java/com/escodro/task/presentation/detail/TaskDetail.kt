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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import com.escodro.task.R
import com.escodro.task.model.AlarmInterval
import com.escodro.task.model.Category
import com.escodro.task.model.Task
import com.escodro.theme.AlkaaTheme
import com.escodro.theme.components.DefaultIconTextContent
import org.koin.androidx.compose.getViewModel
import java.util.Calendar

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
    detailViewModel: TaskDetailViewModel = getViewModel(),
    categoryViewModel: TaskCategoryViewModel = getViewModel(),
    alarmViewModel: TaskAlarmViewModel = getViewModel()
) {
    detailViewModel.setTaskInfo(taskId = taskId)
    val detailViewState by detailViewModel.state

    categoryViewModel.loadCategories()
    val categoryViewState by categoryViewModel.state

    TaskDetailRouter(
        detailViewState = detailViewState,
        categoryViewState = categoryViewState,
        onTitleChanged = detailViewModel::updateTitle,
        onDescriptionChanged = detailViewModel::updateDescription,
        onCategoryChanged = categoryViewModel::updateCategory,
        onAlarmUpdated = { calendar -> alarmViewModel.updateAlarm(taskId, calendar) },
        onIntervalSelected = { interval -> alarmViewModel.setRepeating(taskId, interval) }
    )
}

@Composable
internal fun TaskDetailRouter(
    detailViewState: TaskDetailState,
    categoryViewState: TaskCategoryState,
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onCategoryChanged: (TaskId, CategoryId) -> Unit,
    onAlarmUpdated: (Calendar?) -> Unit,
    onIntervalSelected: (AlarmInterval) -> Unit
) {
    when (detailViewState) {
        TaskDetailState.Error -> TaskDetailError()
        is TaskDetailState.Loaded ->
            TaskDetailContent(
                task = detailViewState.task,
                categoryViewState = categoryViewState,
                onTitleChanged = onTitleChanged,
                onDescriptionChanged = onDescriptionChanged,
                onCategoryChanged = onCategoryChanged,
                onAlarmUpdated = onAlarmUpdated,
                onIntervalSelected = onIntervalSelected
            )
    }
}

@Composable
private fun TaskDetailContent(
    task: Task,
    categoryViewState: TaskCategoryState,
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onCategoryChanged: (TaskId, CategoryId) -> Unit,
    onAlarmUpdated: (Calendar?) -> Unit,
    onIntervalSelected: (AlarmInterval) -> Unit
) {
    Surface(color = MaterialTheme.colors.background) {
        Column {
            TaskTitleTextField(text = task.title, onTitleChanged = onTitleChanged)
            CategorySelection(
                state = categoryViewState,
                currentCategory = task.categoryId,
                onCategoryChanged = { categoryId -> onCategoryChanged(TaskId(task.id), categoryId) }
            )
            TaskDescriptionTextField(
                text = task.description,
                onDescriptionChanged = onDescriptionChanged
            )
            AlarmSelection(
                calendar = task.dueDate,
                interval = task.alarmInterval,
                onAlarmUpdated = onAlarmUpdated,
                onIntervalSelected = onIntervalSelected
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

internal inline class TaskId(val value: Long)

internal inline class CategoryId(val value: Long?)

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
            categoryViewState = TaskCategoryState.Loaded(categories),
            onTitleChanged = {},
            onDescriptionChanged = {},
            onCategoryChanged = { _: TaskId, _: CategoryId -> },
            onAlarmUpdated = {},
            onIntervalSelected = {}
        )
    }
}
