package com.escodro.glance.interactor

import android.content.Context
import androidx.glance.appwidget.updateAll
import com.escodro.domain.interactor.GlanceInteractor
import com.escodro.glance.presentation.TaskListGlanceWidget

internal class GlanceInteractorImpl(private val context: Context) : GlanceInteractor {

    /**
     * Updates all the [TaskListGlanceWidget] to load the latest data.
     */
    override suspend fun onTaskListUpdated() {
        TaskListGlanceWidget().apply {
            loadData()
            updateAll(context)
        }
    }
}
