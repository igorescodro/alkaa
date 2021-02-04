package com.escodro.task.presentation.list

import android.text.format.DateUtils
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.foundation.layout.preferredSize
import androidx.compose.foundation.layout.preferredWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
            TaskListViewState.Empty -> {
                TaskListEmpty()
            }
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

/**
 * Alkaa Task Item.
 *
 * @param modifier the decorator
 * @param task the task item to be rendered
 * @param onItemClicked the action to be done when the item is clicked
 */
@Composable
fun TaskItem(
    modifier: Modifier = Modifier,
    task: TaskWithCategory,
    onItemClicked: (Long) -> Unit,
    onCheckedChanged: (TaskWithCategory) -> Unit
) {
    Card(
        elevation = 4.dp,
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 8.dp)
            .preferredHeight(74.dp)
            .clickable { onItemClicked(task.task.id) }
    ) {
        Row {
            CardRibbon(color = task.category?.color)
            Checkbox(
                modifier = modifier.fillMaxHeight(),
                checked = task.task.completed,
                onCheckedChange = { onCheckedChanged(task) }
            )
            Spacer(Modifier.preferredWidth(8.dp))
            Column(modifier = modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center) {
                Text(
                    text = task.task.title,
                    style = MaterialTheme.typography.body1,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                RelativeDateText(calendar = task.task.dueDate)
            }
        }
    }
}

@Composable
private fun FloatingButton() {
    FloatingActionButton(backgroundColor = MaterialTheme.colors.primary, onClick = { /*TODO*/ }) {
        Icon(
            imageVector = Icons.Outlined.Add,
            contentDescription = stringResource(id = R.string.task_content_description_add_task)
        )
    }
}

@Composable
private fun CardRibbon(color: Color?, modifier: Modifier = Modifier) {
    val ribbonColor = color ?: MaterialTheme.colors.background

    Spacer(
        modifier
            .preferredWidth(18.dp)
            .fillMaxHeight()
            .padding(end = 8.dp)
            .background(ribbonColor)
    )
}

@Composable
private fun RelativeDateText(calendar: Calendar?) {
    if (calendar == null) {
        return
    }

    val context = AmbientContext.current
    val time = calendar.time.time
    val stringTime = DateUtils.getRelativeDateTimeString(
        context, time, DateUtils.DAY_IN_MILLIS, DateUtils.DAY_IN_MILLIS, 0
    ).toString()

    Text(
        text = stringTime,
        style = MaterialTheme.typography.body2,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1
    )
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
