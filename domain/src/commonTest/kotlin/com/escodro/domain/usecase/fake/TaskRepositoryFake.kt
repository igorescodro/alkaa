package com.escodro.domain.usecase.fake

import com.escodro.domain.model.Task
import com.escodro.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map

internal class TaskRepositoryFake : TaskRepository {

    private val taskMap: MutableMap<Long, Task> = mutableMapOf()

    private val taskFlow = MutableSharedFlow<Unit>(replay = 1).apply { tryEmit(Unit) }

    override suspend fun insertTask(task: Task): Long {
        val id = if (task.id == 0L) {
            taskMap.entries
                .maxByOrNull { it.key }
                ?.run { key.plus(1) }
                ?: 1
        } else {
            task.id
        }

        taskMap[id] = task
        taskFlow.emit(Unit)
        return id
    }

    override suspend fun updateTask(task: Task) {
        taskMap[task.id] = task
        taskFlow.emit(Unit)
    }

    override suspend fun deleteTask(task: Task) {
        taskMap.remove(task.id)
        taskFlow.emit(Unit)
    }

    override suspend fun cleanTable() {
        taskMap.clear()
        taskFlow.emit(Unit)
    }

    override suspend fun findAllTasksWithDueDate(): List<Task> =
        taskMap.filter { entry -> entry.value.dueDate != null }.values.toMutableList()

    override suspend fun findTaskById(taskId: Long): Task? =
        taskMap[taskId]

    override fun findTaskByIdFlow(taskId: Long): Flow<Task?> =
        taskFlow.map { taskMap[taskId] }

    fun findAllTasks(): List<Task> =
        taskMap.values.toList()
}
