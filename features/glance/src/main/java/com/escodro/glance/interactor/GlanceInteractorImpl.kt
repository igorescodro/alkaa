package com.escodro.glance.interactor

import com.escodro.domain.interactor.GlanceInteractor
import com.escodro.glance.presentation.TaskListGlanceWidget

internal class GlanceInteractorImpl : GlanceInteractor {

    /**
     * Updates all the [TaskListGlanceWidget] to load the latest data.
     */
    override suspend fun onTaskListUpdated() {
        TaskListGlanceWidget().loadData()
    }
}
