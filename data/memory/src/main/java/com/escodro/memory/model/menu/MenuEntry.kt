package com.escodro.memory.model.menu

/**
 * Data class to represent a menu entry.
 */
data class MenuEntry(
    val groupId: Int,
    val itemId: Int,
    val order: MenuOrder,
    val stringRes: Int
)
