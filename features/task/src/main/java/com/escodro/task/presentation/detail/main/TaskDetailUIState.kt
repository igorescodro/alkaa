package com.escodro.task.presentation.detail.main

/**
 * Represents the possible UI stated of [TaskDetailFragment].
 */
internal sealed class TaskDetailUIState {

    /**
     * Represents the stated where the task is not completed.
     */
    internal object Uncompleted : TaskDetailUIState()

    /**
     * Represents the stated where the task is completed.
     */
    internal object Completed : TaskDetailUIState()
}
