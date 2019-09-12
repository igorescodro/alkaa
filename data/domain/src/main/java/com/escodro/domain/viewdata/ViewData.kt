package com.escodro.domain.viewdata

import android.os.Parcelable
import java.util.Calendar
import kotlinx.android.parcel.Parcelize

/**
 * Encapsulates the UI representation of database models.
 */
sealed class ViewData {

    /**
     * UI representation of a Category.
     */
    @Parcelize
    data class Category(var id: Long = 0, var name: String?, var color: String?) : Parcelable

    /**
     * UI representation of a Task.
     */
    @Parcelize
    data class Task(
        var id: Long = 0,
        var completed: Boolean = false,
        var title: String,
        var description: String? = null,
        var categoryId: Long? = null,
        var dueDate: Calendar? = null,
        var creationDate: Calendar? = null,
        var completedDate: Calendar? = null
    ) : Parcelable

    /**
     * UI representation of a Task with Category.
     */
    @Parcelize
    data class TaskWithCategory(val task: Task, val category: Category? = null) : Parcelable
}
