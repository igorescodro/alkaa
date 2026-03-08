package com.escodro.task.presentation.v2

import androidx.lifecycle.ViewModel
import com.escodro.coroutines.AppCoroutineScope
import com.escodro.domain.model.Task
import com.escodro.domain.model.TaskWithCategory
import com.escodro.domain.provider.DateTimeProvider
import com.escodro.domain.usecase.category.LoadCategory
import com.escodro.domain.usecase.task.AddTask
import com.escodro.domain.usecase.task.UpdateTaskStatus
import com.escodro.domain.usecase.taskwithcategory.LoadTasksByCategory
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
    private val updateTaskStatus: UpdateTaskStatus,
    private val addTask: AddTask,
    private val dateTimeProvider: DateTimeProvider,
    private val applicationScope: AppCoroutineScope,
) : ViewModel() {

    private val addTaskText = MutableStateFlow("")

    fun loadState(categoryId: Long): Flow<TaskListV2ViewState> = flow {
        emit(TaskListV2ViewState.Loading)

        val category = loadCategory(categoryId)
            ?: run {
                emit(TaskListV2ViewState.Error(IllegalArgumentException("Category not found")))
                return@flow
            }

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
        val sections = buildSections(tasks, today)
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

        for (twc in tasks) {
            val item = twc.toTaskItem()
            val dueDate = twc.task.dueDate
            when {
                twc.task.isCompleted -> completed.add(item)
                dueDate == null -> noDate.add(item)
                dueDate.date < today -> overdue.add(item)
                dueDate.date == today -> todayList.add(item)
                else -> upcoming.add(item)
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

    private fun TaskWithCategory.toTaskItem() = TaskItem(
        id = task.id,
        title = task.title,
        isCompleted = task.isCompleted,
        dueDate = task.dueDate,
    )
}

private const val CategoryPlaceholderEmoji = "📋"
