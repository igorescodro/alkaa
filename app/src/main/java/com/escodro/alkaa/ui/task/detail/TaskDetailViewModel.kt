package com.escodro.alkaa.ui.task.detail

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.escodro.alkaa.data.local.model.Task
import io.reactivex.disposables.CompositeDisposable

/**
 * [ViewModel] responsible to provide information to [com.escodro.alkaa.databinding
 * .FragmentDetailBinding].
 *
 * Created by Igor Escodro on 31/5/18.
 */
class TaskDetailViewModel(private val contract: TaskDetailContract) : ViewModel() {

    var delegate: TaskDetailDelegate? = null

    val task = MutableLiveData<Task>()

    private val compositeDisposable = CompositeDisposable()

    /**
     * Load all categories.
     */
    fun loadCategories() {
        compositeDisposable.add(
            contract.loadAllCategories().subscribe { delegate?.updateCategoryList(it) }
        )
    }

    /**
     * Updates the given task.
     *
     * @param task the task to be updated
     */
    fun updateTask(task: Task) {
        contract.updateTask(task).subscribe()
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}
