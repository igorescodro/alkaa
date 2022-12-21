package com.escodro.task.presentation.list

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.escodro.categoryapi.model.Category
import com.escodro.categoryapi.presentation.CategoryListViewModel
import com.escodro.categoryapi.presentation.CategoryState
import com.escodro.designsystem.AlkaaTheme
import com.escodro.designsystem.components.AddFloatingButton
import com.escodro.designsystem.components.AlkaaLoadingContent
import com.escodro.designsystem.components.DefaultIconTextContent
import com.escodro.task.R
import com.escodro.task.model.Task
import com.escodro.task.model.TaskWithCategory
import com.escodro.task.presentation.category.CategorySelection
import com.escodro.task.presentation.detail.main.CategoryId
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import java.util.Calendar

/**
 * Alkaa Task Section.
 *
 * @param onItemClick action to be called when a item is clicked
 * @param onBottomShow action to be called when the bottom sheet is shown
 * @param modifier the decorator
 */
@Composable
fun TaskListSection(
    onItemClick: (Long) -> Unit,
    onBottomShow: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TaskListLoader(modifier = modifier, onItemClick = onItemClick, onAddClick = onBottomShow)
}

@Composable
private fun TaskListLoader(
    onItemClick: (Long) -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier,
    taskListViewModel: TaskListViewModel = getViewModel(),
    categoryViewModel: CategoryListViewModel = getViewModel(),
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

    val snackbarTitle = stringResource(id = R.string.task_snackbar_message_complete)
    val snackbarButton = stringResource(id = R.string.task_snackbar_button_undo)

    val onShowSnackbar: (TaskWithCategory) -> Unit = { taskWithCategory ->
        coroutineScope.launch {
            val message = String.format(snackbarTitle, taskWithCategory.task.title)
            val snackbarResult = snackbarHostState.showSnackbar(
                message = message,
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
    BoxWithConstraints(modifier = Modifier.padding(start = 8.dp, end = 8.dp)) {
        val cellCount = if (this.maxHeight > maxWidth) 1 else 2
        LazyVerticalGrid(
            columns = GridCells.Fixed(cellCount),
            contentPadding = PaddingValues(bottom = 48.dp),
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

@Suppress("UndocumentedPublicFunction")
@Preview
@Composable
fun TaskListScaffoldLoaded() {
    val task1 = Task(title = "Buy milk", dueDate = null)
    val task2 = Task(title = "Call Mark", dueDate = Calendar.getInstance())
    val task3 = Task(title = "Watch Moonlight", dueDate = Calendar.getInstance())

    val category1 = Category(name = "Books", color = android.graphics.Color.GREEN)
    val category2 = Category(name = "Reminders", color = android.graphics.Color.MAGENTA)

    val taskList = persistentListOf(
        TaskWithCategory(task = task1, category = category1),
        TaskWithCategory(task = task2, category = category2),
        TaskWithCategory(task = task3, category = null),
    )

    val state = TaskListViewState.Loaded(items = taskList)

    AlkaaTheme {
        TaskListScaffold(
            taskHandler = TaskStateHandler(state = state),
            categoryHandler = CategoryStateHandler(),
            modifier = Modifier,
        )
    }
}

@Suppress("UndocumentedPublicFunction")
@Preview
@Composable
fun TaskListScaffoldEmpty() {
    val state = TaskListViewState.Empty

    AlkaaTheme {
        TaskListScaffold(
            taskHandler = TaskStateHandler(state = state),
            categoryHandler = CategoryStateHandler(),
            modifier = Modifier,
        )
    }
}

@Suppress("UndocumentedPublicFunction")
@Preview
@Composable
fun TaskListScaffoldError() {
    val state = TaskListViewState.Error(cause = IllegalAccessException())

    AlkaaTheme {
        TaskListScaffold(
            taskHandler = TaskStateHandler(state = state),
            categoryHandler = CategoryStateHandler(),
            modifier = Modifier,
        )
    }
}
