package com.escodro.task.presentation.list

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Represents the possible [TaskListFragment] states.
 */
sealed class TaskListState : Parcelable {

    /**
     * State to show all the tasks.
     */
    @Parcelize
    object ShowAllTasks : TaskListState()

    /**
     * State to show all the completed tasks.
     */
    @Parcelize
    object ShowCompletedTasks : TaskListState()

    /**
     * State to show all tasks with the specific category id.
     *
     * @param categoryId the category id to show only tasks related to this category
     */
    @Parcelize
    data class ShowTaskByCategory(val categoryId: Long) : TaskListState()
}
