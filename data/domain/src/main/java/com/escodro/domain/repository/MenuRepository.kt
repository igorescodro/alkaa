package com.escodro.domain.repository

import com.escodro.domain.model.menu.MenuEntry

/**
 * Interface to represent the implementation of Menu repository.
 */
interface MenuRepository {

    /**
     * Load all menu entries.
     *
     * @return all menu entries
     */
    suspend fun loadEntries(): List<MenuEntry>
}
