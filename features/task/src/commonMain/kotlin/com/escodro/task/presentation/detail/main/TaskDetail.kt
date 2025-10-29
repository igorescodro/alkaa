package com.escodro.task.presentation.detail.main

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import com.escodro.alarmapi.AlarmPermission
import com.escodro.categoryapi.presentation.CategoryListViewModel
import com.escodro.categoryapi.presentation.CategoryState
import com.escodro.designsystem.components.content.AlkaaLoadingContent
import com.escodro.designsystem.components.content.DefaultIconTextContent
import com.escodro.designsystem.components.icon.LeadingIcon
import com.escodro.designsystem.components.topbar.AlkaaToolbar
import com.escodro.parcelable.CommonParcelable
import com.escodro.parcelable.CommonParcelize
import com.escodro.resources.Res
import com.escodro.resources.task_detail_cd_error
import com.escodro.resources.task_detail_cd_icon_category
import com.escodro.resources.task_detail_cd_icon_description
import com.escodro.resources.task_detail_header_error
import com.escodro.task.model.Task
import com.escodro.task.presentation.category.CategorySelection
import com.escodro.task.presentation.detail.TaskDetailActions
import com.escodro.task.presentation.detail.TaskDetailSectionContent
import com.escodro.task.presentation.detail.alarm.AlarmSelection
import com.escodro.task.presentation.detail.alarm.TaskAlarmViewModel
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import kotlin.jvm.JvmInline

@Suppress("LongParameterList")
@Composable
internal fun TaskDetailScreen(
    isSinglePane: Boolean,
    taskId: Long,
    onUpPress: () -> Unit,
    detailViewModel: TaskDetailViewModel = koinInject(),
    categoryViewModel: CategoryListViewModel = koinInject(),
    alarmViewModel: TaskAlarmViewModel = koinInject(),
    alarmPermission: AlarmPermission = koinInject(),
) {
    val id = TaskId(taskId)
    val detailViewState by
        remember(detailViewModel, taskId) {
            detailViewModel.loadTaskInfo(taskId = id)
        }.collectAsState(initial = TaskDetailState.Loading)

    val categoryViewState by
        remember(categoryViewModel, taskId) {
            categoryViewModel.loadCategories()
        }.collectAsState(initial = CategoryState.Loading)

    val taskDetailActions = TaskDetailActions(
        onTitleChange = { title -> detailViewModel.updateTitle(id, title) },
        onDescriptionChange = { desc -> detailViewModel.updateDescription(id, desc) },
        onCategoryChange = { categoryId -> detailViewModel.updateCategory(id, categoryId) },
        onAlarmChange = { calendar -> alarmViewModel.updateAlarm(id, calendar) },
        onIntervalChange = { interval -> alarmViewModel.setRepeating(id, interval) },
        hasExactAlarmPermission = { alarmPermission.hasExactAlarmPermission() },
        openExactAlarmPermissionScreen = { alarmPermission.openExactAlarmPermissionScreen() },
        openAppSettingsScreen = { alarmPermission.openAppSettings() },
        onUpPress = onUpPress,
    )

    TaskDetailRouter(
        isSinglePane = isSinglePane,
        detailViewState = detailViewState,
        categoryViewState = categoryViewState,
        actions = taskDetailActions,
    )
}

@Composable
internal fun TaskDetailRouter(
    isSinglePane: Boolean,
    detailViewState: TaskDetailState,
    categoryViewState: CategoryState,
    actions: TaskDetailActions,
) {
    Scaffold(topBar = {
        AlkaaToolbar(
            isSinglePane = isSinglePane,
            onUpPress = actions.onUpPress,
        )
    }) { paddingValues ->
        Crossfade(
            targetState = detailViewState,
            modifier = Modifier.padding(paddingValues),
        ) { state ->
            when (state) {
                TaskDetailState.Loading -> AlkaaLoadingContent()
                TaskDetailState.Error -> TaskDetailError()
                is TaskDetailState.Loaded ->
                    TaskDetailContent(
                        task = state.task,
                        categoryViewState = categoryViewState,
                        actions = actions,
                    )
            }
        }
    }
}

@Composable
private fun TaskDetailContent(
    task: Task,
    categoryViewState: CategoryState,
    actions: TaskDetailActions,
) {
    Surface(color = MaterialTheme.colorScheme.background) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            TaskTitleTextField(text = task.title, onTitleChange = actions.onTitleChange)
            TaskDetailSectionContent(
                imageVector = Icons.Outlined.Bookmark,
                contentDescription = stringResource(Res.string.task_detail_cd_icon_category),
            ) {
                CategorySelection(
                    state = categoryViewState,
                    currentCategory = task.categoryId,
                    onCategoryChange = actions.onCategoryChange,
                )
            }
            TaskDescriptionTextField(
                text = task.description,
                onDescriptionChange = actions.onDescriptionChange,
            )
            AlarmSelection(
                calendar = task.dueDate,
                interval = task.alarmInterval,
                onAlarmUpdate = actions.onAlarmChange,
                onIntervalSelect = actions.onIntervalChange,
                hasExactAlarmPermission = actions.hasExactAlarmPermission,
                openExactAlarmPermissionScreen = actions.openExactAlarmPermissionScreen,
                openAppSettingsScreen = actions.openAppSettingsScreen,
            )
        }
    }
}

@Composable
private fun TaskDetailError() {
    DefaultIconTextContent(
        icon = Icons.Outlined.Close,
        iconContentDescription = stringResource(Res.string.task_detail_cd_error),
        header = stringResource(Res.string.task_detail_header_error),
    )
}

@Composable
private fun TaskTitleTextField(text: String, onTitleChange: (String) -> Unit) {
    val textState = remember { mutableStateOf(TextFieldValue(text)) }
    val keyboardController = LocalSoftwareKeyboardController.current

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = textState.value,
        onValueChange = {
            onTitleChange(it.text)
            textState.value = it
        },
        maxLines = 1,
        textStyle = MaterialTheme.typography.headlineMedium,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            disabledContainerColor = MaterialTheme.colorScheme.surface,
        ),
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
                contentDescription = stringResource(Res.string.task_detail_cd_icon_description),
            )
        },
        value = textState.value,
        onValueChange = {
            onDescriptionChange(it.text)
            textState.value = it
        },
        textStyle = MaterialTheme.typography.bodyLarge,
        maxLines = 8,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            disabledContainerColor = MaterialTheme.colorScheme.surface,
        ),
    )
}

@Suppress("ModifierOrder")
@JvmInline
@CommonParcelize
internal value class TaskId(val value: Long) : CommonParcelable

@Suppress("ModifierOrder")
@JvmInline
@CommonParcelize
internal value class CategoryId(val value: Long?) : CommonParcelable
