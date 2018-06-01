package com.escodro.alkaa.ui.detail

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.escodro.alkaa.data.local.model.Task

/**
 * [ViewModel] responsible to provide information to [com.escodro.alkaa.databinding
 * .FragmentDetailBinding].
 *
 * Created by Igor Escodro on 31/5/18.
 */
class DetailViewModel : ViewModel() {

    val task = MutableLiveData<Task>()
}
