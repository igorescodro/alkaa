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
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.escodro.task.R
import com.escodro.task.model.Category
import com.escodro.task.model.Task
import com.escodro.task.model.TaskWithCategory
import com.escodro.task.presentation.add.AddTaskSection
import com.escodro.task.presentation.category.CategorySelection
import com.escodro.task.presentation.category.CategoryState
import com.escodro.task.presentation.category.TaskCategoryViewModel
import com.escodro.task.presentation.detail.main.CategoryId
import com.escodro.theme.AlkaaTheme
import com.escodro.theme.components.AlkaaLoadingContent
import com.escodro.theme.components.DefaultIconTextContent
import kotlinx.coroutines.launch
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
    onItemClicked: (Long) -> Unit,
    bottomSheetContent: (@Composable() () -> Unit) -> Unit,
    sheetState: ModalBottomSheetState
) {
    TaskListLoader(modifier = modifier, onItemClicked = onItemClicked, sheetState = sheetState)
    bottomSheetContent { AddTaskSection(sheetState = sheetState) }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun TaskListLoader(
    modifier: Modifier = Modifier,
    onItemClicked: (Long) -> Unit,
    taskListViewModel: TaskListViewModel = getViewModel(),
    categoryViewModel: TaskCategoryViewModel = getViewModel(),
    sheetState: ModalBottomSheetState
) {
    val (currentCategory, setCategory) = rememberSaveable { mutableStateOf<CategoryId?>(null) }

    val taskViewState by remember(taskListViewModel, currentCategory) {
        taskListViewModel.loadTaskList(currentCategory?.value)
    }.collectAsState(initial = TaskListViewState.Loading)

    val categoryViewState by remember(categoryViewModel) { categoryViewModel.loadCategories() }
        .collectAsState(initial = CategoryState.Loading)

    val taskHandler = TaskStateHandler(
        state = taskViewState,
        onCheckedChanged = taskListViewModel::updateTaskStatus,
        onItemClicked = onItemClicked
    )

    val categoryHandler = CategoryStateHandler(
        state = categoryViewState,
        currentCategory = currentCategory,
        onCategoryChanged = setCategory,
    )

    TaskListScaffold(
        handler = taskHandler,
        categoryHandler = categoryHandler,
        modifier = modifier,
        sheetState = sheetState
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun TaskListScaffold(
    handler: TaskStateHandler,
    categoryHandler: CategoryStateHandler,
    modifier: Modifier = Modifier,
    sheetState: ModalBottomSheetState
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        modifier = modifier.fillMaxSize(),
        backgroundColor = MaterialTheme.colors.background,
        topBar = { TaskFilter(categoryHandler = categoryHandler) },
        floatingActionButton = { FloatingButton { coroutineScope.launch { sheetState.show() } } },
        floatingActionButtonPosition = FabPosition.Center
    ) {
        Crossfade(handler.state) { state ->
            when (state) {
                TaskListViewState.Loading -> AlkaaLoadingContent()
                is TaskListViewState.Error -> TaskListError()
                is TaskListViewState.Loaded -> {
                    val taskList = state.items
                    TaskListContent(taskList, handler.onItemClicked, handler.onCheckedChanged)
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
        onCategoryChanged = categoryHandler.onCategoryChanged,
        modifier = Modifier.padding(start = 16.dp)
    )
}

@Composable
private fun TaskListContent(
    taskList: List<TaskWithCategory>,
    onItemClicked: (Long) -> Unit,
    onCheckedChanged: (TaskWithCategory) -> Unit
) {
    Column(modifier = Modifier.padding(start = 8.dp, end = 8.dp)) {
        LazyColumn(contentPadding = PaddingValues(bottom = 48.dp)) {
            items(
                items = taskList,
                itemContent = { task ->
                    TaskItem(
                        task = task,
                        onItemClicked = onItemClicked,
                        onCheckedChanged = onCheckedChanged
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

    val category1 = Category(name = "Books", color = Color.Green)
    val category2 = Category(name = "Reminders", color = Color.Magenta)

    val taskList = listOf(
        TaskWithCategory(task = task1, category = category1),
        TaskWithCategory(task = task2, category = category2),
        TaskWithCategory(task = task3, category = null)
    )

    val state = TaskListViewState.Loaded(items = taskList)

    AlkaaTheme {
        TaskListScaffold(
            handler = TaskStateHandler(state = state),
            categoryHandler = CategoryStateHandler(),
            modifier = Modifier,
            sheetState = ModalBottomSheetState(ModalBottomSheetValue.Hidden)
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
            handler = TaskStateHandler(state = state),
            categoryHandler = CategoryStateHandler(),
            modifier = Modifier,
            sheetState = ModalBottomSheetState(ModalBottomSheetValue.Hidden)
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
            handler = TaskStateHandler(state = state),
            categoryHandler = CategoryStateHandler(),
            modifier = Modifier,
            sheetState = ModalBottomSheetState(ModalBottomSheetValue.Hidden)
        )
    }
}
