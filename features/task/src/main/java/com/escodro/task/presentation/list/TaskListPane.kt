package com.escodro.task.presentation.list

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.escodro.designsystem.components.AddFloatingButton
import com.escodro.designsystem.components.AlkaaLoadingContent
import com.escodro.designsystem.components.DefaultIconTextContent
import com.escodro.task.R
import com.escodro.task.model.TaskWithCategory
import com.escodro.task.presentation.category.CategorySelection
import kotlinx.collections.immutable.ImmutableList

@Composable
@OptIn(ExperimentalMaterial3Api::class)
@Suppress("LongParameterList")
internal fun SinglePaneTaskList(
    snackbarHostState: SnackbarHostState,
    categoryHandler: CategoryStateHandler,
    taskHandler: TaskStateHandler,
    fabPosition: FabPosition,
    modifier: Modifier = Modifier,
    onShowSnackbar: (TaskWithCategory) -> Unit,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = { TaskFilter(categoryHandler = categoryHandler) },
        floatingActionButton = {
            AddFloatingButton(
                contentDescription = R.string.task_cd_add_task,
                onClick = { taskHandler.onAddClick() },
            )
        },
        floatingActionButtonPosition = fabPosition,
    ) { paddingValues ->
        Crossfade(
            targetState = taskHandler.state,
            modifier = Modifier.padding(paddingValues),
        ) { state ->
            when (state) {
                TaskListViewState.Loading -> AlkaaLoadingContent()
                is TaskListViewState.Error -> TaskListError()
                is TaskListViewState.Loaded -> {
                    val taskList = state.items
                    TaskListContent(
                        taskList = taskList,
                        onItemClick = taskHandler.onItemClick,
                        onCheckedChange = { taskWithCategory ->
                            taskHandler.onCheckedChange(taskWithCategory)
                            onShowSnackbar(taskWithCategory)
                        },
                    )
                }

                TaskListViewState.Empty -> TaskListEmpty()
            }
        }
    }
}

@Composable
private fun TaskFilter(categoryHandler: CategoryStateHandler) {
    CategorySelection(
        state = categoryHandler.state,
        currentCategory = categoryHandler.currentCategory?.value,
        onCategoryChange = categoryHandler.onCategoryChange,
        modifier = Modifier.padding(start = 16.dp),
    )
}

@Composable
private fun TaskListContent(
    taskList: ImmutableList<TaskWithCategory>,
    onItemClick: (Long) -> Unit,
    onCheckedChange: (TaskWithCategory) -> Unit,
) {
    LazyColumn(
        contentPadding = PaddingValues(bottom = 48.dp),
        modifier = Modifier.padding(start = 8.dp, end = 8.dp),
    ) {
        items(
            items = taskList,
            itemContent = { task ->
                TaskItem(
                    task = task,
                    onItemClick = onItemClick,
                    onCheckedChange = onCheckedChange,
                )
            },
        )
    }
}

@Composable
private fun TaskListEmpty() {
    DefaultIconTextContent(
        icon = Icons.Outlined.ThumbUp,
        iconContentDescription = R.string.task_list_cd_empty_list,
        header = R.string.task_list_header_empty,
    )
}

@Composable
private fun TaskListError() {
    DefaultIconTextContent(
        icon = Icons.Outlined.Close,
        iconContentDescription = R.string.task_list_cd_error,
        header = R.string.task_list_header_error,
    )
}
