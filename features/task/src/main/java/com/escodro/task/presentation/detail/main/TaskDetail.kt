package com.escodro.task.presentation.detail.main

import android.os.Parcelable
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Create
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import com.escodro.categoryapi.model.Category
import com.escodro.categoryapi.presentation.CategoryListViewModel
import com.escodro.categoryapi.presentation.CategoryState
import com.escodro.task.R
import com.escodro.task.model.Task
import com.escodro.task.presentation.category.CategorySelection
import com.escodro.task.presentation.detail.LeadingIcon
import com.escodro.task.presentation.detail.TaskDetailActions
import com.escodro.task.presentation.detail.TaskDetailSectionContent
import com.escodro.task.presentation.detail.alarm.AlarmSelection
import com.escodro.task.presentation.detail.alarm.TaskAlarmViewModel
import com.escodro.theme.AlkaaTheme
import com.escodro.theme.components.AlkaaLoadingContent
import com.escodro.theme.components.AlkaaToolbar
import com.escodro.theme.components.DefaultIconTextContent
import kotlinx.parcelize.Parcelize
import org.koin.androidx.compose.getViewModel

/**
 * Alkaa Task Detail Section.
 *
 * @param taskId the id from the task to be shown
 */
@Composable
fun TaskDetailSection(taskId: Long, onUpPressed: () -> Unit) {
    TaskDetailLoader(taskId = taskId, onUpPressed = onUpPressed)
}

@Composable
private fun TaskDetailLoader(
    taskId: Long,
    onUpPressed: () -> Unit,
    detailViewModel: TaskDetailViewModel = getViewModel(),
    categoryViewModel: CategoryListViewModel = getViewModel(),
    alarmViewModel: TaskAlarmViewModel = getViewModel()
) {
    val id = TaskId(taskId)
    val detailViewState by
    remember(detailViewModel, taskId) { detailViewModel.loadTaskInfo(taskId = id) }
        .collectAsState(initial = TaskDetailState.Loading)

    val categoryViewState by
    remember(categoryViewModel, taskId) { categoryViewModel.loadCategories() }
        .collectAsState(initial = CategoryState.Loading)

    val taskDetailActions = TaskDetailActions(
        onTitleChanged = { title -> detailViewModel.updateTitle(id, title) },
        onDescriptionChanged = { desc -> detailViewModel.updateDescription(id, desc) },
        onCategoryChanged = { categoryId -> detailViewModel.updateCategory(id, categoryId) },
        onAlarmUpdated = { calendar -> alarmViewModel.updateAlarm(id, calendar) },
        onIntervalSelected = { interval -> alarmViewModel.setRepeating(id, interval) },
        onUpPressed = onUpPressed
    )

    TaskDetailRouter(
        detailViewState = detailViewState,
        categoryViewState = categoryViewState,
        actions = taskDetailActions
    )
}

@Composable
internal fun TaskDetailRouter(
    detailViewState: TaskDetailState,
    categoryViewState: CategoryState,
    actions: TaskDetailActions
) {
    Scaffold(topBar = { AlkaaToolbar(onUpPressed = actions.onUpPressed) }) {
        Crossfade(detailViewState) { state ->
            when (state) {
                TaskDetailState.Loading -> AlkaaLoadingContent()
                TaskDetailState.Error -> TaskDetailError()
                is TaskDetailState.Loaded ->
                    TaskDetailContent(
                        task = state.task,
                        categoryViewState = categoryViewState,
                        actions = actions
                    )
            }
        }
    }
}

@Composable
private fun TaskDetailContent(
    task: Task,
    categoryViewState: CategoryState,
    actions: TaskDetailActions
) {
    Surface(color = MaterialTheme.colors.background) {
        Column {
            TaskTitleTextField(text = task.title, onTitleChanged = actions.onTitleChanged)
            TaskDetailSectionContent(
                imageVector = Icons.Outlined.Create,
                contentDescription = R.string.task_detail_cd_icon_category,
            ) {
                CategorySelection(
                    state = categoryViewState,
                    currentCategory = task.categoryId,
                    onCategoryChanged = actions.onCategoryChanged
                )
            }
            TaskDescriptionTextField(
                text = task.description,
                onDescriptionChanged = actions.onDescriptionChanged
            )
            AlarmSelection(
                calendar = task.dueDate,
                interval = task.alarmInterval,
                onAlarmUpdated = actions.onAlarmUpdated,
                onIntervalSelected = actions.onIntervalSelected
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
        colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.surface)
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
        colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.surface)
    )
}

@Parcelize
internal inline class TaskId(val value: Long) : Parcelable

@Parcelize
internal inline class CategoryId(val value: Long?) : Parcelable

@Suppress("UndocumentedPublicFunction")
@Preview
@Composable
fun TaskDetailPreview() {
    val task = Task(title = "Buy milk", description = "This is a amazing task!", dueDate = null)
    val category1 = Category(name = "Groceries", color = android.graphics.Color.CYAN)
    val category2 = Category(name = "Books", color = android.graphics.Color.RED)
    val category3 = Category(name = "Movies", color = android.graphics.Color.MAGENTA)

    val categories = listOf(category1, category2, category3)

    AlkaaTheme {
        TaskDetailContent(
            task = task,
            categoryViewState = CategoryState.Loaded(categories),
            actions = TaskDetailActions()
        )
    }
}
