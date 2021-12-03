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
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import com.escodro.alarmapi.AlarmPermission
import com.escodro.categoryapi.model.Category
import com.escodro.categoryapi.presentation.CategoryListViewModel
import com.escodro.categoryapi.presentation.CategoryState
import com.escodro.designsystem.AlkaaTheme
import com.escodro.designsystem.components.AlkaaLoadingContent
import com.escodro.designsystem.components.AlkaaToolbar
import com.escodro.designsystem.components.DefaultIconTextContent
import com.escodro.task.R
import com.escodro.task.model.Task
import com.escodro.task.presentation.category.CategorySelection
import com.escodro.task.presentation.detail.LeadingIcon
import com.escodro.task.presentation.detail.TaskDetailActions
import com.escodro.task.presentation.detail.TaskDetailSectionContent
import com.escodro.task.presentation.detail.alarm.AlarmSelection
import com.escodro.task.presentation.detail.alarm.TaskAlarmViewModel
import kotlinx.parcelize.Parcelize
import org.koin.androidx.compose.get
import org.koin.androidx.compose.getViewModel

/**
 * Alkaa Task Detail Section.
 *
 * @param taskId the id from the task to be shown
 * @param onUpPress action to be called when the back button is pressed
 */
@Composable
fun TaskDetailSection(taskId: Long, onUpPress: () -> Unit) {
    TaskDetailLoader(taskId = taskId, onUpPress = onUpPress)
}

@Suppress("LongParameterList")
@Composable
private fun TaskDetailLoader(
    taskId: Long,
    onUpPress: () -> Unit,
    detailViewModel: TaskDetailViewModel = getViewModel(),
    categoryViewModel: CategoryListViewModel = getViewModel(),
    alarmViewModel: TaskAlarmViewModel = getViewModel(),
    alarmPermission: AlarmPermission = get()
) {
    val id = TaskId(taskId)
    val detailViewState by
    remember(detailViewModel, taskId) { detailViewModel.loadTaskInfo(taskId = id) }
        .collectAsState(initial = TaskDetailState.Loading)

    val categoryViewState by
    remember(categoryViewModel, taskId) { categoryViewModel.loadCategories() }
        .collectAsState(initial = CategoryState.Loading)

    val taskDetailActions = TaskDetailActions(
        onTitleChange = { title -> detailViewModel.updateTitle(id, title) },
        onDescriptionChange = { desc -> detailViewModel.updateDescription(id, desc) },
        onCategoryChange = { categoryId -> detailViewModel.updateCategory(id, categoryId) },
        onAlarmUpdate = { calendar -> alarmViewModel.updateAlarm(id, calendar) },
        onIntervalSelect = { interval -> alarmViewModel.setRepeating(id, interval) },
        hasAlarmPermission = { alarmPermission.hasExactAlarmPermission() },
        onUpPress = onUpPress
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
    Scaffold(topBar = { AlkaaToolbar(onUpPress = actions.onUpPress) }) {
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
            TaskTitleTextField(text = task.title, onTitleChange = actions.onTitleChange)
            TaskDetailSectionContent(
                imageVector = Icons.Outlined.Bookmark,
                contentDescription = R.string.task_detail_cd_icon_category,
            ) {
                CategorySelection(
                    state = categoryViewState,
                    currentCategory = task.categoryId,
                    onCategoryChange = actions.onCategoryChange
                )
            }
            TaskDescriptionTextField(
                text = task.description,
                onDescriptionChange = actions.onDescriptionChange
            )
            AlarmSelection(
                calendar = task.dueDate,
                interval = task.alarmInterval,
                onAlarmUpdate = actions.onAlarmUpdate,
                onIntervalSelect = actions.onIntervalSelect,
                hasAlarmPermission = actions.hasAlarmPermission
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
private fun TaskTitleTextField(text: String, onTitleChange: (String) -> Unit) {
    val textState = remember { mutableStateOf(TextFieldValue(text)) }

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = textState.value,
        onValueChange = {
            onTitleChange(it.text)
            textState.value = it
        },
        textStyle = MaterialTheme.typography.h4,
        colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.surface)
    )
}

@Composable
private fun TaskDescriptionTextField(text: String?, onDescriptionChange: (String) -> Unit) {
    val textState = remember { mutableStateOf(TextFieldValue(text ?: "")) }

    TextField(
        modifier = Modifier.fillMaxWidth(),
        leadingIcon = {
            LeadingIcon(
                imageVector = Icons.Default.Description,
                contentDescription = R.string.task_detail_cd_icon_description
            )
        },
        value = textState.value,
        onValueChange = {
            onDescriptionChange(it.text)
            textState.value = it
        },
        textStyle = MaterialTheme.typography.body1,
        colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.surface)
    )
}

@Suppress("ModifierOrder")
@JvmInline
@Parcelize
internal value class TaskId(val value: Long) : Parcelable

@Suppress("ModifierOrder")
@JvmInline
@Parcelize
internal value class CategoryId(val value: Long?) : Parcelable

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
