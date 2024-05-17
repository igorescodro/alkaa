package com.escodro.task.presentation.detail.alarm.interactor

import com.escodro.task.presentation.detail.alarm.AlarmSelectionState
import kotlinx.coroutines.CoroutineScope

interface OpenAlarmScheduler {

    operator fun invoke(
        coroutineScope: CoroutineScope,
        alarmSelectionState: AlarmSelectionState,
        hasExactAlarmPermission: () -> Boolean,
    )
}
