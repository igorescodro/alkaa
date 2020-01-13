package com.escodro.memory

import com.escodro.memory.model.menu.MenuEntry
import com.escodro.memory.model.menu.MenuOrder

/**
 * Loads all the Menu information in memory.
 */
internal class MenuLoader {

    /**
     * Loads all the Menu information in memory.
     *
     * @return the Menu information
     */
    fun load(): List<MenuEntry> = listOf(
        MenuEntry(GROUP_TASKS, ALL_TASKS_ITEM, MenuOrder.NONE, R.string.app_name),
        MenuEntry(GROUP_TASKS, COMPLETED_ITEM, MenuOrder.NONE, R.string.app_name),
        MenuEntry(GROUP_SETTINGS, CATEGORY_ITEM, MenuOrder.NONE, R.string.app_name),
        MenuEntry(GROUP_SETTINGS, TRACKER_ITEM, MenuOrder.NONE, R.string.app_name)
    )

    companion object {

        // Group IDs
        private const val GROUP_TASKS = 1
        private const val GROUP_SETTINGS = 2

        // Item IDs
        private const val ALL_TASKS_ITEM = 0
        private const val COMPLETED_ITEM = -1
        private const val CATEGORY_ITEM = -2
        private const val TRACKER_ITEM = -3
    }
}
