package com.escodro.task.model

data class ChecklistItem(
    val id: Long = 0,
    val taskId: Long,
    val title: String,
    val isCompleted: Boolean = false,
)
