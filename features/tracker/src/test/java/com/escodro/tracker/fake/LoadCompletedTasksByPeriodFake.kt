package com.escodro.tracker.fake

import com.escodro.domain.model.Category
import com.escodro.domain.model.Task
import com.escodro.domain.model.TaskWithCategory
import com.escodro.domain.usecase.tracker.LoadCompletedTasksByPeriod
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.util.Calendar

internal class LoadCompletedTasksByPeriodFake : LoadCompletedTasksByPeriod {

    private var returnList: List<TaskWithCategory> = emptyList()

    var throwError: Boolean = false

    fun clearList() {
        returnList = emptyList()
    }

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

        returnList = taskList
    }

    fun clean() {
        returnList = emptyList()
        throwError = false
    }

    override fun invoke(): Flow<List<TaskWithCategory>> =
        if (throwError) {
            flowOf(returnList).map { throw IllegalAccessException() }
        } else {
            flowOf(returnList)
        }
}
