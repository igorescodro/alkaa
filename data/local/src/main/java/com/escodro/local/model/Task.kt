package com.escodro.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Calendar

/**
 * Entity to represent a task.
 *
 * @property id unique task id
 * @property completed indicates if the task is completed
 * @property title the task title
 * @property description the task description
 * @property categoryId the associated category id
 * @property dueDate the due date to the task be notified
 * @property creationDate the date of creation of the task
 * @property completedDate the date of completion of the task
 * @property isRepeating indicates if the task is repeating
 * @property alarmInterval the interval between the repeating
 */
@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["category_id"],
            childColumns = ["task_category_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["task_category_id"])]
)
data class Task(
    @ColumnInfo(name = "task_id") @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @ColumnInfo(name = "task_is_completed") var completed: Boolean = false,
    @ColumnInfo(name = "task_title") var title: String,
    @ColumnInfo(name = "task_description") var description: String? = null,
    @ColumnInfo(name = "task_category_id") var categoryId: Long? = null,
    @ColumnInfo(name = "task_due_date") var dueDate: Calendar? = null,
    @ColumnInfo(name = "task_creation_date") var creationDate: Calendar? = null,
    @ColumnInfo(name = "task_completed_date") var completedDate: Calendar? = null,
    @ColumnInfo(name = "task_is_repeating") var isRepeating: Boolean = false,
    @ColumnInfo(name = "task_alarm_interval") var alarmInterval: AlarmInterval? = null
)
