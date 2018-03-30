package com.escodro.alkaa.data.local.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 *
 * [Entity] to represent a task.
 *
 * @author Igor Escodro on 1/2/18.
 */
@Entity
data class Task(
        @ColumnInfo(name = "completed_flag") var completed: Boolean = false,
        @ColumnInfo(name = "task_description") var description: String?) {

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
