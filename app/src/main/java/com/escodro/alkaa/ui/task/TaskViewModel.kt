package com.escodro.alkaa.ui.task

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.databinding.ObservableField
import com.escodro.alkaa.data.local.model.Task
import io.reactivex.disposables.CompositeDisposable

/**
 * [ViewModel] responsible to provide information to [com.escodro.alkaa.databinding
 * .ActivityTaskBinding].
 *
 * Created by Igor Escodro on 1/2/18.
 */
class TaskViewModel(private val navigator: TaskNavigator) : ViewModel() {

    private val contract: TaskContract = TaskContract()

    private val compositeDisposable = CompositeDisposable()

    val newTask: ObservableField<String> = ObservableField()

    /**
     * Loads all tasks.
     */
    fun loadTasks() {
        compositeDisposable.clear()
        compositeDisposable.add(contract.loadTasks().subscribe({ navigator.updateList(it) }))
    }

    /**
     * Add a new task.
     */
    fun addTask() {
        val task = Task(description = newTask.get())
        compositeDisposable.clear()
        compositeDisposable.addAll(contract.addTask(task)
                ?.doOnComplete({ cleanAndLoadTasks() })
                ?.subscribe())
    }

    /**
     * Updates the task status.
     */
    fun updateTaskStatus(task: Task, isCompleted: Boolean) {
        compositeDisposable.clear()
        task.completed = isCompleted
        compositeDisposable.add(contract.updateTask(task).subscribe())

    }

    /**
     * Life-cycle method to be called [android.support.v7.app.AppCompatActivity.onDestroy].
     */
    fun onDestroy() =
            compositeDisposable.clear()

    private fun cleanAndLoadTasks() {
        newTask.set("")
        loadTasks()
    }

    /**
     * A creator to build the [TaskViewModel] passing the [TaskNavigator] as paramenter.
     */
    class Factory(private val navigator: TaskNavigator) :
            ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return TaskViewModel(navigator) as T
        }
    }
}
