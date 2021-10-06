package com.escodro.domain.usecase.alarm

import com.escodro.domain.usecase.fake.AlarmPermissionInteractorFake
import org.junit.Assert
import org.junit.Test

internal class ShowAlarmPermissionInfoTest {

    private val permissionInteractor = AlarmPermissionInteractorFake()

    private val showPermissionUseCase = ShowAlarmPermissionInfo(permissionInteractor)

    @Test
    fun `test if permission was requested`() {
        // when the use case is called
        showPermissionUseCase()

        // the assert the interactor was called
        Assert.assertTrue(permissionInteractor.wasRequested)
    }
}
