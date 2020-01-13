package com.escodro.memory.datasource

import com.escodro.memory.MenuLoader
import com.escodro.memory.mapper.menu.MenuEntryMapper
import com.escodro.repository.datasource.MenuDataSource
import com.escodro.repository.model.menu.MenuEntry

internal class MenuMemoryDataSource(
    private val loader: MenuLoader,
    private val mapper: MenuEntryMapper
) : MenuDataSource {

    override suspend fun loadEntries(): List<MenuEntry> =
        mapper.toRepo(loader.load())
}
