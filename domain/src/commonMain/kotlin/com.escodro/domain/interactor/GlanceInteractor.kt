package com.escodro.domain.interactor

/**
 * Interface to interact with the glance feature.
 */
interface GlanceInteractor {

    /**
     * Function called when the Task List receives a significant update that should reflect in the
     * glance/widget.
     */
    suspend fun onTaskListUpdated()
}
