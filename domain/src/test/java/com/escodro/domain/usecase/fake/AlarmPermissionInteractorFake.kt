package com.escodro.domain.usecase.fake

import com.escodro.domain.interactor.AlarmPermissionInteractor

internal class AlarmPermissionInteractorFake : AlarmPermissionInteractor {

    var wasRequested = false

    override fun requestPermission() {
        wasRequested = true
    }
}
