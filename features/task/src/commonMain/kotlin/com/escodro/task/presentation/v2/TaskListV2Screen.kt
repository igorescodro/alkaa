package com.escodro.task.presentation.v2

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.escodro.designsystem.components.content.AlkaaLoadingContent
import com.escodro.designsystem.components.content.DefaultIconTextContent
import com.escodro.designsystem.components.kuvio.bar.KuvioAddTaskBar
import com.escodro.resources.Res
import com.escodro.resources.task_list_v2_cd_back
import com.escodro.resources.task_list_v2_cd_empty
import com.escodro.resources.task_list_v2_cd_error
import com.escodro.resources.task_list_v2_cd_more_options
import com.escodro.resources.task_list_v2_header_empty
import com.escodro.resources.task_list_v2_header_error
import com.escodro.resources.task_list_v2_section_completed
import com.escodro.resources.task_list_v2_section_no_date
import com.escodro.resources.task_list_v2_section_overdue
import com.escodro.resources.task_list_v2_section_today
import com.escodro.resources.task_list_v2_section_upcoming
import com.escodro.resources.task_list_v2_subtitle
import com.escodro.task.presentation.list.TaskItem
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TaskListV2Section(
    categoryId: Long,
    onBack: () -> Unit,
    onTaskClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: TaskListV2ViewModel = koinViewModel()
    val state by remember(viewModel, categoryId) {
        viewModel.loadState(categoryId)
    }.collectAsState(initial = TaskListV2ViewState.Loading)

    TaskListV2Scaffold(
        state = state,
        onBack = onBack,
        onTaskClick = onTaskClick,
        onAddTaskTextChange = viewModel::onAddTaskTextChange,
        onAddTaskSubmit = { viewModel.onAddTaskSubmit(categoryId) },
        onTaskCheckedChange = { taskId -> viewModel.updateTaskStatus(taskId) },
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TaskListV2Scaffold(
    state: TaskListV2ViewState,
    onBack: () -> Unit,
    onTaskClick: (Long) -> Unit,
    onAddTaskTextChange: (String) -> Unit,
    onAddTaskSubmit: () -> Unit,
    onTaskCheckedChange: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val addTaskText = if (state is TaskListV2ViewState.Loaded) state.addTaskText else ""

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = stringResource(Res.string.task_list_v2_cd_back),
                        )
                    }
                },
            )
        },
        bottomBar = {
            KuvioAddTaskBar(
                value = addTaskText,
                onValueChange = onAddTaskTextChange,
                onAddClick = onAddTaskSubmit,
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            )
        },
    ) { paddingValues ->
        Crossfade(
            targetState = state,
            modifier = Modifier.padding(paddingValues),
        ) { currentState ->
            when (currentState) {
                TaskListV2ViewState.Loading -> AlkaaLoadingContent()
                is TaskListV2ViewState.Error -> DefaultIconTextContent(
                    icon = Icons.Outlined.Close,
                    iconContentDescription = stringResource(Res.string.task_list_v2_cd_error),
                    header = stringResource(Res.string.task_list_v2_header_error),
                )
                is TaskListV2ViewState.Loaded -> {
                    if (currentState.sections.isEmpty()) {
                        DefaultIconTextContent(
                            icon = Icons.Outlined.Close,
                            iconContentDescription = stringResource(Res.string.task_list_v2_cd_empty),
                            header = stringResource(Res.string.task_list_v2_header_empty),
                        )
                    } else {
                        TaskListV2Content(
                            state = currentState,
                            onTaskClick = onTaskClick,
                            onTaskCheckedChange = onTaskCheckedChange,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TaskListV2Content(
    state: TaskListV2ViewState.Loaded,
    onTaskClick: (Long) -> Unit,
    onTaskCheckedChange: (Long) -> Unit,
) {
    LazyColumn(
        contentPadding = PaddingValues(bottom = 80.dp),
        modifier = Modifier.fillMaxSize(),
    ) {
        item {
            CategoryHeader(
                emoji = state.categoryEmoji,
                name = state.categoryName,
                subtitle = stringResource(
                    Res.string.task_list_v2_subtitle,
                    state.totalCount,
                    state.completedCount,
                ),
            )
        }

        state.sections.forEach { section ->
            stickyHeader(key = section.type) {
                SectionHeader(type = section.type)
            }
            items(
                items = section.tasks,
                key = { it.id },
            ) { task ->
                TaskListV2Item(
                    task = task,
                    onTaskClick = onTaskClick,
                    onTaskCheckedChange = onTaskCheckedChange,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryHeader(
    emoji: String,
    name: String,
    subtitle: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = emoji, style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.padding(horizontal = 12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = name, style = MaterialTheme.typography.titleLarge)
            Text(text = subtitle, style = MaterialTheme.typography.bodyMedium)
        }
        IconButton(onClick = {}) {
            Icon(
                imageVector = Icons.Outlined.MoreVert,
                contentDescription = stringResource(Res.string.task_list_v2_cd_more_options),
            )
        }
    }
}

@Composable
private fun SectionHeader(type: TaskSectionType, modifier: Modifier = Modifier) {
    val label = when (type) {
        TaskSectionType.OVERDUE -> stringResource(Res.string.task_list_v2_section_overdue)
        TaskSectionType.TODAY -> stringResource(Res.string.task_list_v2_section_today)
        TaskSectionType.UPCOMING -> stringResource(Res.string.task_list_v2_section_upcoming)
        TaskSectionType.COMPLETED -> stringResource(Res.string.task_list_v2_section_completed)
        TaskSectionType.NO_DATE -> stringResource(Res.string.task_list_v2_section_no_date)
    }
    Text(
        text = label,
        style = MaterialTheme.typography.labelLarge,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
    )
}

@Composable
private fun TaskListV2Item(
    task: TaskItem,
    onTaskClick: (Long) -> Unit,
    onTaskCheckedChange: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    TaskItem(
        task = com.escodro.task.model.TaskWithCategory(
            task = com.escodro.task.model.Task(
                id = task.id,
                title = task.title,
                isCompleted = task.isCompleted,
                dueDate = task.dueDate,
            ),
        ),
        onItemClick = onTaskClick,
        onCheckedChange = { twc -> onTaskCheckedChange(twc.task.id) },
        modifier = modifier,
    )
}
