@file:Suppress("TooManyFunctions") // Preview need separate light/dark functions for now

package com.escodro.category.presentation.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.escodro.categoryapi.model.Category
import com.escodro.designsystem.components.kuvio.bar.KuvioAddTaskBar
import com.escodro.designsystem.components.kuvio.header.KuvioCategoryHeader
import com.escodro.designsystem.components.kuvio.item.KuvioTaskItem
import com.escodro.designsystem.components.kuvio.item.KuvioTaskItemData
import com.escodro.designsystem.components.kuvio.item.KuvioTaskItemState
import com.escodro.designsystem.components.kuvio.text.KuvioBodyMediumText
import com.escodro.designsystem.components.kuvio.text.KuvioTitleMediumText
import com.escodro.designsystem.components.picker.DateTimerPicker
import com.escodro.designsystem.components.topbar.AlkaaToolbar
import com.escodro.designsystem.theme.AlkaaThemePreview
import com.escodro.domain.model.Task
import com.escodro.domain.model.TaskGroup
import com.escodro.resources.Res
import com.escodro.resources.category_details_empty_description
import com.escodro.resources.category_details_empty_title
import com.escodro.resources.category_details_section_completed
import com.escodro.resources.category_details_section_due_today
import com.escodro.resources.category_details_section_no_due_date
import com.escodro.resources.category_details_section_overdue
import com.escodro.resources.category_details_section_upcoming
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.LocalDateTime
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

/**
 * Alkaa Category Details Section.
 *
 * Public entry point for the Category Details screen. Stateless — delegates immediately to
 * [CategoryDetailsLoader].
 *
 * @param categoryId the ID of the category to display.
 * @param isSinglePane whether the layout is in single-pane mode (phones) or multi-pane (tablets).
 * @param onBackClick callback invoked when the user navigates back.
 * @param onTaskClick callback invoked when the user taps a task item.
 * @param modifier modifier applied to the outermost layout.
 */
@Composable
fun CategoryDetailsSection(
    categoryId: Long,
    isSinglePane: Boolean,
    onBackClick: () -> Unit,
    onTaskClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    CategoryDetailsLoader(
        categoryId = categoryId,
        isSinglePane = isSinglePane,
        onBackClick = onBackClick,
        onTaskClick = onTaskClick,
        modifier = modifier,
    )
}

@Composable
@Suppress("LongParameterList")
internal fun CategoryDetailsLoader(
    categoryId: Long,
    isSinglePane: Boolean,
    onBackClick: () -> Unit,
    onTaskClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CategoryDetailsViewModel = koinInject(),
) {
    val state by remember(categoryId) {
        viewModel.loadContent(categoryId)
    }.collectAsState(initial = CategoryDetailsState.Loading)

    CategoryDetailsScreen(
        state = state,
        isSinglePane = isSinglePane,
        onAddTask = { title, dueDate -> viewModel.addTask(title, dueDate, categoryId) },
        onUpdateTaskStatus = { taskId -> viewModel.updateTaskStatus(taskId) },
        onTaskClick = onTaskClick,
        onOptionsClick = {},
        onBackClick = onBackClick,
        modifier = modifier,
    )
}

@Composable
@Suppress("LongParameterList")
internal fun CategoryDetailsScreen(
    state: CategoryDetailsState,
    isSinglePane: Boolean,
    onAddTask: (String, LocalDateTime?) -> Unit,
    onUpdateTaskStatus: (Long) -> Unit,
    onTaskClick: (Long) -> Unit,
    onOptionsClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            AlkaaToolbar(isSinglePane = isSinglePane, onUpPress = onBackClick)
        },
        modifier = modifier,
    ) { paddingValues ->
        when (state) {
            is CategoryDetailsState.Loading -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.padding(paddingValues).fillMaxSize(),
                ) {
                    CircularProgressIndicator()
                }
            }

            is CategoryDetailsState.Error -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.padding(paddingValues).fillMaxSize(),
                ) {
                    KuvioBodyMediumText(
                        text = state.throwable.message ?: "An error occurred",
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }

            is CategoryDetailsState.Success -> {
                CategoryDetailsContent(
                    category = state.data.category,
                    categoryColor = state.data.categoryColor,
                    groups = state.data.groups,
                    totalTasks = state.data.totalTasks,
                    completedTasks = state.data.completedTasks,
                    onAddTask = onAddTask,
                    onUpdateTaskStatus = onUpdateTaskStatus,
                    onTaskClick = onTaskClick,
                    onOptionsClick = onOptionsClick,
                    modifier = Modifier.padding(paddingValues),
                )
            }
        }
    }
}

@Composable
@Suppress("LongParameterList")
internal fun CategoryDetailsContent(
    category: Category,
    categoryColor: Color,
    groups: ImmutableList<TaskGroup>,
    totalTasks: Int,
    completedTasks: Int,
    onAddTask: (String, LocalDateTime?) -> Unit,
    onUpdateTaskStatus: (Long) -> Unit,
    onTaskClick: (Long) -> Unit,
    onOptionsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var taskTitle by rememberSaveable { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf<LocalDateTime?>(null) }
    var isDatePickerOpen by rememberSaveable { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxSize()) {
        KuvioCategoryHeader(
            name = category.name,
            color = categoryColor,
            totalTasks = totalTasks,
            completedTasks = completedTasks,
            onOptionsClick = onOptionsClick,
        )

        if (groups.isEmpty()) {
            CategoryDetailsEmptyState(modifier = Modifier.weight(1f).fillMaxWidth())
        } else {
            CategoryDetailsTaskList(
                groups = groups,
                categoryColor = categoryColor,
                onUpdateTaskStatus = onUpdateTaskStatus,
                onTaskClick = onTaskClick,
                modifier = Modifier.weight(1f),
            )
        }

        KuvioAddTaskBar(
            value = taskTitle,
            onValueChange = { taskTitle = it },
            onAddClick = {
                onAddTask(taskTitle, selectedDate)
                taskTitle = ""
                selectedDate = null
            },
            onDateClick = { isDatePickerOpen = true },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
        )
    }

    DateTimerPicker(
        initialDateTime = selectedDate,
        isDialogOpen = isDatePickerOpen,
        onCloseDialog = { isDatePickerOpen = false },
        onDateChange = { date ->
            selectedDate = date
            isDatePickerOpen = false
        },
    )
}

@Composable
private fun CategoryDetailsEmptyState(modifier: Modifier = Modifier) {
    Box(contentAlignment = Alignment.Center, modifier = modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            KuvioTitleMediumText(text = stringResource(Res.string.category_details_empty_title))
            KuvioBodyMediumText(
                text = stringResource(Res.string.category_details_empty_description),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun CategoryDetailsTaskList(
    groups: ImmutableList<TaskGroup>,
    categoryColor: Color,
    onUpdateTaskStatus: (Long) -> Unit,
    onTaskClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        groups.forEach { group ->
            val taskState = when (group) {
                is TaskGroup.Overdue -> {
                    KuvioTaskItemState.OVERDUE
                }

                is TaskGroup.Completed -> {
                    KuvioTaskItemState.COMPLETED
                }

                is TaskGroup.DueToday,
                is TaskGroup.Upcoming,
                is TaskGroup.NoDueDate,
                -> {
                    KuvioTaskItemState.PENDING
                }
            }

            if (group.tasks.isNotEmpty()) {
                item(key = "header_${group::class.simpleName}") {
                    val sectionTitle = when (group) {
                        is TaskGroup.Overdue -> stringResource(Res.string.category_details_section_overdue)
                        is TaskGroup.DueToday -> stringResource(Res.string.category_details_section_due_today)
                        is TaskGroup.Upcoming -> stringResource(Res.string.category_details_section_upcoming)
                        is TaskGroup.NoDueDate -> stringResource(Res.string.category_details_section_no_due_date)
                        is TaskGroup.Completed -> stringResource(Res.string.category_details_section_completed)
                    }
                    KuvioTitleMediumText(
                        text = sectionTitle,
                        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp),
                    )
                }

                items(items = group.tasks, key = { task -> task.id }) { task ->
                    KuvioTaskItem(
                        data = KuvioTaskItemData(
                            title = task.title,
                            state = taskState,
                            categoryColor = categoryColor,
                        ),
                        onItemClick = { onTaskClick(task.id) },
                        onCheckClick = { onUpdateTaskStatus(task.id) },
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CategoryDetailsContentEmptyLightPreview() {
    AlkaaThemePreview {
        CategoryDetailsContent(
            category = Category(id = 1L, name = "Work", color = 0xFF6200EA.toInt()),
            categoryColor = Color(0xFF6200EA),
            groups = persistentListOf(),
            totalTasks = 0,
            completedTasks = 0,
            onAddTask = { _, _ -> },
            onUpdateTaskStatus = {},
            onTaskClick = {},
            onOptionsClick = {},
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F1B2D)
@Composable
private fun CategoryDetailsContentEmptyDarkPreview() {
    AlkaaThemePreview(isDarkTheme = true) {
        CategoryDetailsContent(
            category = Category(id = 1L, name = "Work", color = 0xFF6200EA.toInt()),
            categoryColor = Color(0xFF6200EA),
            groups = persistentListOf(),
            totalTasks = 0,
            completedTasks = 0,
            onAddTask = { _, _ -> },
            onUpdateTaskStatus = {},
            onTaskClick = {},
            onOptionsClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CategoryDetailsContentWithTasksLightPreview() {
    val tasks = listOf(
        Task(id = 1L, title = "Design the mockups"),
        Task(id = 2L, title = "Review pull request"),
    )
    AlkaaThemePreview {
        CategoryDetailsContent(
            category = Category(id = 1L, name = "Work", color = 0xFF6200EA.toInt()),
            categoryColor = Color(0xFF6200EA),
            groups = persistentListOf(TaskGroup.NoDueDate(tasks)),
            totalTasks = 2,
            completedTasks = 0,
            onAddTask = { _, _ -> },
            onUpdateTaskStatus = {},
            onTaskClick = {},
            onOptionsClick = {},
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F1B2D)
@Composable
private fun CategoryDetailsContentWithTasksDarkPreview() {
    val tasks = listOf(
        Task(id = 1L, title = "Design the mockups"),
        Task(id = 2L, title = "Review pull request"),
    )
    AlkaaThemePreview(isDarkTheme = true) {
        CategoryDetailsContent(
            category = Category(id = 1L, name = "Work", color = 0xFF6200EA.toInt()),
            categoryColor = Color(0xFF6200EA),
            groups = persistentListOf(TaskGroup.NoDueDate(tasks)),
            totalTasks = 2,
            completedTasks = 0,
            onAddTask = { _, _ -> },
            onUpdateTaskStatus = {},
            onTaskClick = {},
            onOptionsClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CategoryDetailsEmptyStateLightPreview() {
    AlkaaThemePreview {
        CategoryDetailsEmptyState()
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F1B2D)
@Composable
private fun CategoryDetailsEmptyStateDarkPreview() {
    AlkaaThemePreview(isDarkTheme = true) {
        CategoryDetailsEmptyState()
    }
}
