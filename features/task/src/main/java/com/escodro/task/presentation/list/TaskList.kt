package com.escodro.task.presentation.list

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FabPosition
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.escodro.categoryapi.model.Category
import com.escodro.categoryapi.presentation.CategoryListViewModel
import com.escodro.categoryapi.presentation.CategoryState
import com.escodro.task.R
import com.escodro.task.model.Task
import com.escodro.task.model.TaskWithCategory
import com.escodro.task.presentation.category.CategorySelection
import com.escodro.task.presentation.detail.main.CategoryId
import com.escodro.theme.AlkaaTheme
import com.escodro.theme.components.AddFloatingButton
import com.escodro.theme.components.AlkaaLoadingContent
import com.escodro.theme.components.DefaultIconTextContent
import org.koin.androidx.compose.getViewModel
import java.util.Calendar

/**
 * Alkaa Task Section.
 *
 * @param modifier the decorator
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TaskListSection(
    modifier: Modifier = Modifier,
    onItemClick: (Long) -> Unit,
    onBottomShow: () -> Unit
) {
    TaskListLoader(modifier = modifier, onItemClick = onItemClick, onAddClick = onBottomShow)
}

@Composable
private fun TaskListLoader(
    modifier: Modifier = Modifier,
    onItemClick: (Long) -> Unit,
    onAddClick: () -> Unit,
    taskListViewModel: TaskListViewModel = getViewModel(),
    categoryViewModel: CategoryListViewModel = getViewModel(),
) {
    val (currentCategory, setCategory) = rememberSaveable { mutableStateOf<CategoryId?>(null) }

    val taskViewState by remember(taskListViewModel, currentCategory) {
        taskListViewModel.loadTaskList(currentCategory?.value)
    }.collectAsState(initial = TaskListViewState.Loading)

    val categoryViewState by remember(categoryViewModel) { categoryViewModel.loadCategories() }
        .collectAsState(initial = CategoryState.Loading)

    val taskHandler = TaskStateHandler(
        state = taskViewState,
        onCheckedChange = taskListViewModel::updateTaskStatus,
        onItemClick = onItemClick,
        onAddClick = onAddClick
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

@Composable
internal fun TaskListScaffold(
    taskHandler: TaskStateHandler,
    categoryHandler: CategoryStateHandler,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        backgroundColor = MaterialTheme.colors.background,
        topBar = { TaskFilter(categoryHandler = categoryHandler) },
        floatingActionButton = {
            AddFloatingButton(
                contentDescription = R.string.task_cd_add_task,
                onClick = { taskHandler.onAddClick() }
            )
        },
        floatingActionButtonPosition = FabPosition.Center
    ) {
        Crossfade(taskHandler.state) { state ->
            when (state) {
                TaskListViewState.Loading -> AlkaaLoadingContent()
                is TaskListViewState.Error -> TaskListError()
                is TaskListViewState.Loaded -> {
                    val taskList = state.items
                    TaskListContent(
                        taskList,
                        taskHandler.onItemClick,
                        taskHandler.onCheckedChange
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
        modifier = Modifier.padding(start = 16.dp)
    )
}

@Composable
private fun TaskListContent(
    taskList: List<TaskWithCategory>,
    onItemClick: (Long) -> Unit,
    onCheckedChange: (TaskWithCategory) -> Unit
) {
    Column(modifier = Modifier.padding(start = 8.dp, end = 8.dp)) {
        LazyColumn(contentPadding = PaddingValues(bottom = 48.dp)) {
            items(
                items = taskList,
                itemContent = { task ->
                    TaskItem(
                        task = task,
                        onItemClick = onItemClick,
                        onCheckedChange = onCheckedChange
                    )
                }
            )
        }
    }
}

@Composable
private fun TaskListEmpty() {
    DefaultIconTextContent(
        icon = Icons.Outlined.ThumbUp,
        iconContentDescription = R.string.task_list_cd_empty_list,
        header = R.string.task_list_header_empty
    )
}

@Composable
private fun TaskListError() {
    DefaultIconTextContent(
        icon = Icons.Outlined.Close,
        iconContentDescription = R.string.task_list_cd_error,
        header = R.string.task_list_header_error
    )
}

@ExperimentalMaterialApi
@Suppress("UndocumentedPublicFunction")
@Preview
@Composable
fun TaskListScaffoldLoaded() {
    val task1 = Task(title = "Buy milk", dueDate = null)
    val task2 = Task(title = "Call Mark", dueDate = Calendar.getInstance())
    val task3 = Task(title = "Watch Moonlight", dueDate = Calendar.getInstance())

    val category1 = Category(name = "Books", color = android.graphics.Color.GREEN)
    val category2 = Category(name = "Reminders", color = android.graphics.Color.MAGENTA)

    val taskList = listOf(
        TaskWithCategory(task = task1, category = category1),
        TaskWithCategory(task = task2, category = category2),
        TaskWithCategory(task = task3, category = null)
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

@OptIn(ExperimentalMaterialApi::class)
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

@OptIn(ExperimentalMaterialApi::class)
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
