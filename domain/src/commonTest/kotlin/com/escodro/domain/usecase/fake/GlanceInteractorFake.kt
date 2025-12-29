package com.escodro.domain.usecase.fake

import com.escodro.domain.interactor.GlanceInteractor

internal class GlanceInteractorFake : GlanceInteractor {

    var isNotified: Boolean = false

    override suspend fun onTaskListUpdated() {
        isNotified = true
    }

    fun clean() {
        isNotified = false
    }
}
