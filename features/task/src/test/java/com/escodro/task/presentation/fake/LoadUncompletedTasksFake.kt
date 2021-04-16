package com.escodro.task.presentation.fake

import com.escodro.domain.model.Category
import com.escodro.domain.model.Task
import com.escodro.domain.model.TaskWithCategory
import com.escodro.domain.usecase.taskwithcategory.LoadUncompletedTasks
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.util.Calendar

internal class LoadUncompletedTasksFake : LoadUncompletedTasks {

    private var list: List<TaskWithCategory> = listOf()

    var throwError: Boolean = false

    fun returnDefaultValues() {
        val task1 = Task(title = "Buy milk", dueDate = null)
        val task2 = Task(title = "Call Mark", dueDate = Calendar.getInstance())
        val task3 = Task(title = "Watch Moonlight", dueDate = Calendar.getInstance())
        val task4 = Task(title = "Find Moo")

        val category1 = Category(id = 1, name = "Books", color = "#FF0000")
        val category2 = Category(id = 2, name = "Reminders", color = "#00FF00")

        val taskList = listOf(
            TaskWithCategory(task = task1, category = category1),
            TaskWithCategory(task = task2, category = category2),
            TaskWithCategory(task = task3, category = null),
            TaskWithCategory(task = task4, category = category1)
        )

        list = taskList
    }

    fun returnValues(numberOfValues: Int) {
        val task = Task(title = "Buy milk", dueDate = null)
        val category = Category(name = "Books", color = "#FF0000")

        val taskList = mutableListOf<TaskWithCategory>()
        for (i in 1..numberOfValues) {
            taskList.add(TaskWithCategory(task = task.copy(id = i.toLong()), category = category))
        }

        list = taskList
    }

    fun clean() {
        list = listOf()
        throwError = false
    }

    override fun invoke(categoryId: Long?): Flow<List<TaskWithCategory>> {
        return when {
            throwError -> flowOf(list).map { throw IllegalStateException() }
            categoryId != null -> flowOf(list.filter { it.category?.id == categoryId })
            else -> flowOf(list)
        }
    }
}
