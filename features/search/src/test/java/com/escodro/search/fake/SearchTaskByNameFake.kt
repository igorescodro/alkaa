package com.escodro.search.fake

import com.escodro.domain.model.Category
import com.escodro.domain.model.Task
import com.escodro.domain.model.TaskWithCategory
import com.escodro.domain.usecase.search.SearchTasksByName
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class SearchTaskByNameFake : SearchTasksByName {

    private var list: List<TaskWithCategory> = listOf()

    fun returnValues(numberOfValues: Int) {
        val task = Task(title = "Buy milk", dueDate = null)
        val category = Category(name = "Books", color = "#FF0000")

        val taskList = mutableListOf<TaskWithCategory>()
        for (i in 1..numberOfValues) {
            taskList.add(TaskWithCategory(task = task.copy(id = i.toLong()), category = category))
        }

        list = taskList
    }

    override suspend fun invoke(query: String): Flow<List<TaskWithCategory>> =
        flow { emit(list) }
}
