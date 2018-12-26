package com.escodro.alkaa.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * [ViewModel] to share information between the [MainActivity] and [com.escodro.alkaa.ui.task
 * .list.TaskListFragment].
 */
class MainTaskViewModel : ViewModel() {

    /**
     * [MutableLiveData] to be observed by the [MainActivity] and set the action bar title when
     * updated.
     */
    val actionBarTitle = MutableLiveData<String>()

    /**
     * Updates the action bar title.
     *
     * @param title the title to be updated
     */
    fun updateTitle(title: String) {
        actionBarTitle.value = title
    }
}
