package com.escodro.task.presentation.v2

import androidx.lifecycle.ViewModel
import com.escodro.coroutines.AppCoroutineScope
import com.escodro.domain.model.Task
import com.escodro.domain.model.TaskWithCategory
import com.escodro.domain.provider.DateTimeProvider
import com.escodro.domain.usecase.category.LoadCategory
import com.escodro.domain.usecase.task.AddTask
import com.escodro.domain.usecase.taskwithcategory.LoadTasksByCategory
import com.escodro.task.mapper.TaskItemMapper
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalDate

internal class TaskListV2ViewModel(
    private val loadTasksByCategory: LoadTasksByCategory,
    private val loadCategory: LoadCategory,
    private val addTask: AddTask,
    private val dateTimeProvider: DateTimeProvider,
    private val applicationScope: AppCoroutineScope,
    private val taskItemMapper: TaskItemMapper,
) : ViewModel() {

    private val addTaskText = MutableStateFlow("")

    fun loadState(categoryId: Long): Flow<TaskListV2ViewState> = flow {
        emit(TaskListV2ViewState.Loading)

        val category = loadCategory(categoryId)
        if (category == null) {
            emit(TaskListV2ViewState.Error(IllegalArgumentException("Category not found")))
        } else {
            combine(
                loadTasksByCategory(categoryId),
                addTaskText,
            ) { tasks, text ->
                buildLoadedState(
                    categoryName = category.name,
                    tasks = tasks,
                    addTaskText = text,
                )
            }
                .catch { error -> emit(TaskListV2ViewState.Error(error)) }
                .collect { state -> emit(state) }
        }
    }

    fun onAddTaskTextChange(text: String) {
        addTaskText.update { text }
    }

    fun onAddTaskSubmit(categoryId: Long) {
        val title = addTaskText.value.trim()
        if (title.isBlank()) return
        applicationScope.launch {
            addTask(Task(title = title, categoryId = categoryId))
            addTaskText.update { "" }
        }
    }

    fun updateTaskStatus(taskId: Long) {
        applicationScope.launch {
            updateTaskStatus(taskId)
        }
    }

    private fun buildLoadedState(
        categoryName: String,
        tasks: List<TaskWithCategory>,
        addTaskText: String,
    ): TaskListV2ViewState.Loaded {
        val today = dateTimeProvider.getCurrentLocalDateTime().date
        val sections = buildSections(tasks = tasks, today = today)
        return TaskListV2ViewState.Loaded(
            categoryName = categoryName,
            categoryEmoji = CategoryPlaceholderEmoji,
            totalCount = tasks.size,
            completedCount = tasks.count { it.task.isCompleted },
            sections = sections,
            addTaskText = addTaskText,
        )
    }

    private fun buildSections(
        tasks: List<TaskWithCategory>,
        today: LocalDate,
    ): ImmutableList<TaskSection> {
        val overdue = mutableListOf<TaskItem>()
        val todayList = mutableListOf<TaskItem>()
        val upcoming = mutableListOf<TaskItem>()
        val completed = mutableListOf<TaskItem>()
        val noDate = mutableListOf<TaskItem>()

        for (taskWithCategory in tasks) {
            val dueDate = taskWithCategory.task.dueDate
            val sectionType = when {
                taskWithCategory.task.isCompleted -> TaskSectionType.COMPLETED
                dueDate == null -> TaskSectionType.NO_DATE
                dueDate.date < today -> TaskSectionType.OVERDUE
                dueDate.date == today -> TaskSectionType.TODAY
                else -> TaskSectionType.UPCOMING
            }
            val item = taskItemMapper.toTaskItem(taskWithCategory, sectionType)
            when (sectionType) {
                TaskSectionType.OVERDUE -> overdue.add(item)
                TaskSectionType.TODAY -> todayList.add(item)
                TaskSectionType.UPCOMING -> upcoming.add(item)
                TaskSectionType.COMPLETED -> completed.add(item)
                TaskSectionType.NO_DATE -> noDate.add(item)
            }
        }

        return listOf(
            TaskSectionType.OVERDUE to overdue,
            TaskSectionType.TODAY to todayList,
            TaskSectionType.UPCOMING to upcoming,
            TaskSectionType.COMPLETED to completed,
            TaskSectionType.NO_DATE to noDate,
        )
            .filter { (_, items) -> items.isNotEmpty() }
            .map { (type, items) -> TaskSection(type = type, tasks = items.toImmutableList()) }
            .toImmutableList()
    }
}

private const val CategoryPlaceholderEmoji = "📋"
