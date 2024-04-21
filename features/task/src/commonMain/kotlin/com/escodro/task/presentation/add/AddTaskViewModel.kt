package com.escodro.task.presentation.add

import com.escodro.coroutines.AppCoroutineScope
import com.escodro.domain.model.Task
import com.escodro.domain.usecase.task.AddTask
import com.escodro.task.mapper.AlarmIntervalMapper
import com.escodro.task.model.AlarmInterval
import com.escodro.task.presentation.detail.main.CategoryId
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.datetime.LocalDateTime

internal class AddTaskViewModel(
    private val addTaskUseCase: AddTask,
    private val alarmIntervalMapper: AlarmIntervalMapper,
    private val applicationScope: AppCoroutineScope,
) : ViewModel() {

    fun addTask(
        title: String,
        categoryId: CategoryId?,
        dueDate: LocalDateTime?,
        alarmInterval: AlarmInterval = AlarmInterval.NEVER,
    ) {
        if (title.isBlank()) return

        val interval = alarmIntervalMapper.toDomain(alarmInterval)
        applicationScope.launch {
            val task = Task(
                title = title,
                dueDate = dueDate,
                categoryId = categoryId?.value,
                alarmInterval = interval,
            )
            addTaskUseCase.invoke(task)
        }
    }
}
