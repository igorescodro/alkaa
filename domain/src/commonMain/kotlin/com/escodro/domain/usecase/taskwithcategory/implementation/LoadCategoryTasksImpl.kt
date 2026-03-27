package com.escodro.domain.usecase.taskwithcategory.implementation

import com.escodro.domain.model.Task
import com.escodro.domain.model.TaskGroup
import com.escodro.domain.provider.DateTimeProvider
import com.escodro.domain.repository.TaskWithCategoryRepository
import com.escodro.domain.usecase.taskwithcategory.LoadCategoryTasks
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class LoadCategoryTasksImpl(
    private val repository: TaskWithCategoryRepository,
    private val dateTimeProvider: DateTimeProvider,
) : LoadCategoryTasks {

    override fun invoke(categoryId: Long): Flow<List<TaskGroup>> =
        repository.findAllTasksWithCategoryId(categoryId).map { list ->
            val tasks = list.map { it.task }
            val today = dateTimeProvider.getCurrentLocalDateTime().date
            buildGroups(tasks, today)
        }

    private fun buildGroups(tasks: List<Task>, today: kotlinx.datetime.LocalDate): List<TaskGroup> {
        val completed = tasks.filter { it.isCompleted }
        val active = tasks.filter { !it.isCompleted }

        val noDueDate = active.filter { it.dueDate == null }
        val overdue = active.filter { it.dueDate != null && it.dueDate.date < today }
        val dueToday = active.filter { it.dueDate != null && it.dueDate.date == today }
        val upcoming = active.filter { it.dueDate != null && it.dueDate.date > today }

        return listOfNotNull(
            TaskGroup.Overdue(overdue).takeIf { overdue.isNotEmpty() },
            TaskGroup.DueToday(dueToday).takeIf { dueToday.isNotEmpty() },
            TaskGroup.Upcoming(upcoming).takeIf { upcoming.isNotEmpty() },
            TaskGroup.NoDueDate(noDueDate).takeIf { noDueDate.isNotEmpty() },
            TaskGroup.Completed(completed).takeIf { completed.isNotEmpty() },
        )
    }
}
