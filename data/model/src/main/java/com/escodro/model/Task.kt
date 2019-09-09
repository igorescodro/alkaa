package com.escodro.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Calendar

/**
 * [Entity] to represent a task.
 */
@Entity(
    foreignKeys = [ForeignKey(
        entity = Category::class,
        parentColumns = ["category_id"],
        childColumns = ["task_category_id"],
        onDelete = ForeignKey.CASCADE
    )],
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
    @ColumnInfo(name = "task_completed_date") var completedDate: Calendar? = null
)
