package com.escodro.task.fake

import com.escodro.task.presentation.detail.alarm.AlarmSelectionState
import com.escodro.task.presentation.detail.alarm.interactor.OpenAlarmScheduler
import kotlinx.coroutines.CoroutineScope

internal class OpenAlarmSchedulerFake : OpenAlarmScheduler {
    override fun invoke(
        coroutineScope: CoroutineScope,
        alarmSelectionState: AlarmSelectionState,
        hasExactAlarmPermission: () -> Boolean
    ) {
        // Do nothing
    }
}
