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
import com.escodro.domain.model.TaskGroup
import com.escodro.resources.Res
import com.escodro.resources.category_details_empty_description
import com.escodro.resources.category_details_empty_title
import com.escodro.resources.category_details_section_completed
import com.escodro.resources.category_details_section_due_today
import com.escodro.resources.category_details_section_no_due_date
import com.escodro.resources.category_details_section_overdue
import com.escodro.resources.category_details_section_upcoming
import kotlinx.datetime.LocalDateTime
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

/**
 * Alkaa Category Details Section.
 *
 * Entry point for the Category Details screen. Loads state from [CategoryDetailsViewModel]
 * and delegates rendering to [CategoryDetailsScreen].
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
    val viewModel: CategoryDetailsViewModel = koinInject()
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
    when (state) {
        is CategoryDetailsState.Loading -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier.fillMaxSize(),
            ) {
                CircularProgressIndicator()
            }
        }

        is CategoryDetailsState.Error -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier.fillMaxSize(),
            ) {
                KuvioBodyMediumText(
                    text = state.throwable.message ?: "An error occurred",
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }

        is CategoryDetailsState.Success -> {
            CategoryDetailsContent(
                category = state.category,
                categoryColor = state.categoryColor,
                groups = state.groups,
                totalTasks = state.totalTasks,
                completedTasks = state.completedTasks,
                onAddTask = onAddTask,
                onUpdateTaskStatus = onUpdateTaskStatus,
                onTaskClick = onTaskClick,
                onOptionsClick = onOptionsClick,
                onBackClick = onBackClick,
                modifier = modifier,
            )
        }
    }
}

@Composable
@Suppress("LongParameterList", "UnusedParameter")
internal fun CategoryDetailsContent(
    category: Category,
    categoryColor: Color,
    groups: List<TaskGroup>,
    totalTasks: Int,
    completedTasks: Int,
    onAddTask: (String, LocalDateTime?) -> Unit,
    onUpdateTaskStatus: (Long) -> Unit,
    onTaskClick: (Long) -> Unit,
    onOptionsClick: () -> Unit,
    onBackClick: () -> Unit,
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
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    KuvioTitleMediumText(
                        text = stringResource(Res.string.category_details_empty_title),
                    )
                    KuvioBodyMediumText(
                        text = stringResource(Res.string.category_details_empty_description),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                groups.forEach { group ->
                    val taskState = when (group) {
                        is TaskGroup.Overdue -> KuvioTaskItemState.OVERDUE
                        is TaskGroup.Completed -> KuvioTaskItemState.COMPLETED
                        else -> KuvioTaskItemState.PENDING
                    }

                    if (group.tasks.isNotEmpty()) {
                        item(key = "header_${group::class.simpleName}") {
                            val sectionTitle = when (group) {
                                is TaskGroup.Overdue ->
                                    stringResource(Res.string.category_details_section_overdue)
                                is TaskGroup.DueToday ->
                                    stringResource(Res.string.category_details_section_due_today)
                                is TaskGroup.Upcoming ->
                                    stringResource(Res.string.category_details_section_upcoming)
                                is TaskGroup.NoDueDate ->
                                    stringResource(Res.string.category_details_section_no_due_date)
                                is TaskGroup.Completed ->
                                    stringResource(Res.string.category_details_section_completed)
                            }
                            KuvioTitleMediumText(
                                text = sectionTitle,
                                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp),
                            )
                        }

                        items(
                            items = group.tasks,
                            key = { task -> task.id },
                        ) { task ->
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
