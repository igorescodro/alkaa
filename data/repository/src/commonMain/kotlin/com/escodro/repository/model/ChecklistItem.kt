package com.escodro.repository.model

/**
 * Data class to represent a Checklist Item.
 *
 * @property id unique checklist item id
 * @property taskId the associated task id
 * @property title the checklist item title
 * @property isCompleted indicates if the checklist item is completed
 */
data class ChecklistItem(
    val id: Long = 0,
    val taskId: Long,
    val title: String,
    val isCompleted: Boolean = false,
)
