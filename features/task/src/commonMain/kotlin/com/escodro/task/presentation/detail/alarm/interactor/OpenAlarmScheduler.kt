package com.escodro.task.presentation.detail.alarm.interactor

import com.escodro.task.presentation.detail.alarm.AlarmSelectionState
import kotlinx.coroutines.CoroutineScope

/**
 * Interactor to abstract the action to check the permissions and open the alarm scheduler. Event
 * though a multiplatform library is used, in the iOS Test Emulator, it doesn't work properly and we
 * need a fake implementation. This also makes it easy to ignore the permission state during tests.
 */
interface OpenAlarmScheduler {

    operator fun invoke(
        coroutineScope: CoroutineScope,
        alarmSelectionState: AlarmSelectionState,
        hasExactAlarmPermission: () -> Boolean,
    )
}
