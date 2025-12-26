package com.escodro.glance.interactor

import android.content.Context
import com.escodro.domain.interactor.GlanceInteractor
import com.escodro.glance.data.TaskListUpdaterWorker
import com.escodro.glance.presentation.TaskListGlanceWidget

internal class GlanceInteractorImpl(private val context: Context) : GlanceInteractor {

    /**
     * Updates all the [TaskListGlanceWidget] to load the latest data.
     */
    override suspend fun onTaskListUpdated() {
        TaskListUpdaterWorker.enqueue(context)
    }
}
