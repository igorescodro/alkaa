package com.escodro.domain.usecase.fake

import com.escodro.domain.model.Task
import com.escodro.domain.repository.TaskRepository
import java.util.TreeMap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class TaskRepositoryFake : TaskRepository {

    private val taskMap: TreeMap<Long, Task> = TreeMap()

    override suspend fun insertTask(task: Task) {
        val id = if (task.id == 0L) {
            taskMap.lastKey() + 1
        } else {
            task.id
        }

        taskMap[id] = task
    }

    override suspend fun updateTask(task: Task) {
        taskMap[task.id] = task
    }

    override suspend fun deleteTask(task: Task) {
        taskMap.remove(task.id)
    }

    override suspend fun cleanTable() {
        taskMap.clear()
    }

    override suspend fun findAllTasksWithDueDate(): List<Task> =
        taskMap.filter { entry -> entry.value.dueDate != null }.values.toMutableList()

    @Suppress("LabeledExpression")
    override fun findTaskFlowById(taskId: Long): Flow<Task> =
        flow {
            val task = taskMap[taskId] ?: return@flow
            emit(task)
        }

    override suspend fun findTaskById(taskId: Long): Task =
        taskMap[taskId] ?: throw IllegalArgumentException("Task does not exist")
}
