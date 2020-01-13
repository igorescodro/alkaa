package com.escodro.domain.usecase.menu

import com.escodro.domain.model.menu.MenuEntry
import com.escodro.domain.repository.MenuRepository

/**
 * Use case to load all the menu entries available.
 */
class LoadMenuEntries(private val menuRepository: MenuRepository) {

    /**
     * Loads all the menu entries available.
     *
     * @return a list with the menu entries
     */
    suspend operator fun invoke(): List<MenuEntry> =
        menuRepository.loadEntries()
}
