package com.escodro.alkaa.ui.task

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.text.TextUtils
import com.escodro.alkaa.data.local.model.Task
import io.reactivex.disposables.CompositeDisposable

/**
 * [ViewModel] responsible to provide information to [com.escodro.alkaa.databinding
 * .ActivityTaskBinding].
 *
 * Created by Igor Escodro on 1/2/18.
 */
class TaskViewModel(private val contract: TaskContract, private val navigator: TaskNavigator) :
        ViewModel() {

    val newTask = MutableLiveData<String>()

    private var compositeDisposable = CompositeDisposable()

    /**
     * Loads all tasks.
     */
    fun loadTasks() {
        compositeDisposable.add(
                contract.loadTasks().subscribe({ navigator.updateList(it) }))
    }

    /**
     * Add a new task.
     */
    fun addTask() {
        val description = newTask.value
        if (TextUtils.isEmpty(description)) {
            navigator.onEmptyField()
            return
        }

        val task = Task(description = newTask.value)
        contract.addTask(task)
                ?.doOnComplete({ onNewTaskAdded(task) })
                ?.subscribe()
    }

    /**
     * Updates the task status.
     *
     * @param task task to be updated
     */
    fun updateTaskStatus(task: Task, isCompleted: Boolean) {
        task.completed = isCompleted
        contract.updateTask(task).subscribe()

    }

    /**
     * Deletes the given task.
     *
     * @param task task to be removed
     */
    fun deleteTask(task: Task) {
        contract.deleteTask(task)
                .doOnComplete({ onTaskRemoved(task) })
                .subscribe()

    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    private fun onNewTaskAdded(task: Task) {
        newTask.value = ""
        navigator.onNewTaskAdded(task)
    }

    private fun onTaskRemoved(task: Task) {
        navigator.onTaskRemoved(task)
    }
}
