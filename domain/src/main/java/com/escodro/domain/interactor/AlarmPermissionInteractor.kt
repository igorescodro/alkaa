package com.escodro.domain.interactor

/**
 * Contract to interact with the Alarm layer.
 */
interface AlarmPermissionInteractor {

    /**
     * Informs and requests the alarm permission to the user.
     */
    fun requestPermission()
}
