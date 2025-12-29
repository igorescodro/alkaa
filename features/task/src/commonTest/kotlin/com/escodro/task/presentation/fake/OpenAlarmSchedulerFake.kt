package com.escodro.task.presentation.fake

import com.escodro.task.presentation.detail.alarm.AlarmSelectionState
import com.escodro.task.presentation.detail.alarm.interactor.OpenAlarmScheduler
import kotlinx.coroutines.CoroutineScope

class OpenAlarmSchedulerFake : OpenAlarmScheduler {
    override fun invoke(
        coroutineScope: CoroutineScope,
        alarmSelectionState: AlarmSelectionState,
        hasExactAlarmPermission: () -> Boolean,
    ) = Unit
}
