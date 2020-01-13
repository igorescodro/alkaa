package com.escodro.repository

import com.escodro.domain.model.menu.MenuEntry
import com.escodro.domain.repository.MenuRepository
import com.escodro.repository.datasource.MenuDataSource
import com.escodro.repository.mapper.menu.MenuEntryMapper

internal class MenuRepositoryImpl(
    private val menuDataSource: MenuDataSource,
    private val menuEntryMapper: MenuEntryMapper
) : MenuRepository {

    override suspend fun loadEntries(): List<MenuEntry> =
        menuEntryMapper.toDomain(menuDataSource.loadEntries())
}
