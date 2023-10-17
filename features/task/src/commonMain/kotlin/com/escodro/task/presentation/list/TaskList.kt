package com.escodro.task.presentation.list

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.escodro.categoryapi.presentation.CategoryListViewModel
import com.escodro.categoryapi.presentation.CategoryState
import com.escodro.designsystem.components.AddFloatingButton
import com.escodro.designsystem.components.AlkaaLoadingContent
import com.escodro.designsystem.components.DefaultIconTextContent
import com.escodro.navigation.AlkaaDestinations
import com.escodro.resources.MR
import com.escodro.task.model.TaskWithCategory
import com.escodro.task.presentation.category.CategorySelection
import com.escodro.task.presentation.detail.main.CategoryId
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

/**
 * Alkaa Task Section.
 *
 * @param modifier the decorator
 */
@Composable
fun TaskListSection(modifier: Modifier = Modifier) {
    val navigator = LocalNavigator.currentOrThrow
    val bottomSheetNavigator = LocalBottomSheetNavigator.current

    TaskListLoader(
        modifier = modifier,
        onItemClick = { taskId ->
            val screen = ScreenRegistry.get(AlkaaDestinations.Task.TaskDetail(taskId))
            navigator.push(screen)
        },
        onAddClick = {
            val screen = ScreenRegistry.get(AlkaaDestinations.Task.AddTaskBottomSheet)
            bottomSheetNavigator.show(screen)
        },
    )
}

@Composable
internal fun TaskListLoader(
    onItemClick: (Long) -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier,
    taskListViewModel: TaskListViewModel = koinInject(),
    categoryViewModel: CategoryListViewModel = koinInject(),
) {
    val (currentCategory, setCategory) = rememberSaveable { mutableStateOf<CategoryId?>(null) }

    val taskViewState by remember(taskListViewModel, currentCategory) {
        taskListViewModel.loadTaskList(currentCategory?.value)
    }.collectAsState(initial = TaskListViewState.Loading)

    val categoryViewState by remember(categoryViewModel) {
        categoryViewModel.loadCategories()
    }.collectAsState(initial = CategoryState.Loading)

    val taskHandler = TaskStateHandler(
        state = taskViewState,
        onCheckedChange = taskListViewModel::updateTaskStatus,
        onItemClick = onItemClick,
        onAddClick = onAddClick,
    )

    val categoryHandler = CategoryStateHandler(
        state = categoryViewState,
        currentCategory = currentCategory,
        onCategoryChange = setCategory,
    )

    TaskListScaffold(
        taskHandler = taskHandler,
        categoryHandler = categoryHandler,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TaskListScaffold(
    taskHandler: TaskStateHandler,
    categoryHandler: CategoryStateHandler,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val snackbarTitle = stringResource(MR.strings.task_snackbar_message_complete)
    val snackbarButton = stringResource(MR.strings.task_snackbar_button_undo)

    val onShowSnackbar: (TaskWithCategory) -> Unit = { taskWithCategory ->
        coroutineScope.launch {
            val snackbarResult = snackbarHostState.showSnackbar(
                message = snackbarTitle,
                actionLabel = snackbarButton,
                duration = SnackbarDuration.Short,
            )
            when (snackbarResult) {
                SnackbarResult.Dismissed -> {} // Do nothing
                SnackbarResult.ActionPerformed -> taskHandler.onCheckedChange(taskWithCategory)
            }
        }
    }

    BoxWithConstraints {
        val fabPosition = if (this.maxHeight > maxWidth) FabPosition.Center else FabPosition.End
        Scaffold(
            modifier = modifier.fillMaxSize(),
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            topBar = { TaskFilter(categoryHandler = categoryHandler) },
            floatingActionButton = {
                AddFloatingButton(
                    contentDescription = stringResource(MR.strings.task_cd_add_task),
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
        iconContentDescription = stringResource(MR.strings.task_list_cd_empty_list),
        header = stringResource(MR.strings.task_list_header_empty),
    )
}

@Composable
private fun TaskListError() {
    DefaultIconTextContent(
        icon = Icons.Outlined.Close,
        iconContentDescription = stringResource(MR.strings.task_list_cd_error),
        header = stringResource(MR.strings.task_list_header_error),
    )
}
