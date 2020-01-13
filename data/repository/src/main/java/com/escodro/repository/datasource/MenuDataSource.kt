package com.escodro.repository.datasource

import com.escodro.repository.model.menu.MenuEntry

/**
 * Interface to represent the implementation of Menu data source.
 */
interface MenuDataSource {

    /**
     * Load all menu entries.
     *
     * @return all menu entries
     */
    suspend fun loadEntries(): List<MenuEntry>
}
