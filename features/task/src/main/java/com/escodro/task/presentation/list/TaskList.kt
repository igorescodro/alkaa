package com.escodro.task.presentation.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FabPosition
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.escodro.task.R
import com.escodro.task.model.Category
import com.escodro.task.model.Task
import com.escodro.task.model.TaskWithCategory
import com.escodro.theme.AlkaaTheme
import org.koin.androidx.compose.getViewModel
import java.util.Calendar

/**
 * Alkaa Task Section.
 *
 * @param modifier the decorator
 */
@Composable
fun TaskListSection(modifier: Modifier = Modifier, onItemClicked: (Long) -> Unit) {
    TaskListLoader(modifier = modifier, onItemClicked = onItemClicked)
}

@Composable
private fun TaskListLoader(
    modifier: Modifier = Modifier,
    onItemClicked: (Long) -> Unit,
    viewModel: TaskListViewModel = getViewModel()
) {
    viewModel.loadTasks()
    val viewState by viewModel.state.collectAsState()

    TaskListScaffold(
        viewState = viewState,
        modifier = modifier,
        onCheckedChanged = { item -> viewModel.updateTaskStatus(item) },
        onItemClicked = onItemClicked
    )
}

@Composable
private fun TaskListScaffold(
    viewState: TaskListViewState,
    modifier: Modifier = Modifier,
    onCheckedChanged: (TaskWithCategory) -> Unit,
    onItemClicked: (Long) -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        backgroundColor = MaterialTheme.colors.background,
        floatingActionButton = { FloatingButton() },
        floatingActionButtonPosition = FabPosition.Center
    ) {
        when (viewState) {
            is TaskListViewState.Error -> {
                /** TODO **/
            }
            is TaskListViewState.Loaded -> {
                val taskList = viewState.items
                TaskListContent(taskList, onItemClicked, onCheckedChanged)
            }
            TaskListViewState.Empty -> TaskListEmpty()
        }
    }
}

@Composable
private fun TaskListContent(
    taskList: List<TaskWithCategory>,
    onItemClicked: (Long) -> Unit,
    onCheckedChanged: (TaskWithCategory) -> Unit
) {
    Column(modifier = Modifier.padding(start = 8.dp, end = 8.dp)) {
        LazyColumn {
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
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Outlined.ThumbUp,
            contentDescription = stringResource(id = R.string.task_content_description_empty_list),
            modifier = Modifier.preferredSize(128.dp)
        )
        Text(
            text = stringResource(id = R.string.task_header_empty_list),
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.Center
        )
    }
}

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
            viewState = state,
            modifier = Modifier,
            onCheckedChanged = {},
            onItemClicked = {}
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
            viewState = state,
            modifier = Modifier,
            onCheckedChanged = {},
            onItemClicked = {}
        )
    }
}
