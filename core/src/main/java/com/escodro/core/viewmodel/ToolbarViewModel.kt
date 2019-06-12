package com.escodro.core.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * [ViewModel] to rename Main Activity Toolbar from another modules.
 */
class ToolbarViewModel : ViewModel() {

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
    fun updateTitle(title: String?) {
        actionBarTitle.value = title
    }
}
