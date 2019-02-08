package com.escodro.alkaa.ui.task.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.escodro.alkaa.R

/**
 * [Fragment] responsible to show the [com.escodro.alkaa.data.local.model.Category] options .
 */
class TaskCategoryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_task_detail_category, container, false)
}
