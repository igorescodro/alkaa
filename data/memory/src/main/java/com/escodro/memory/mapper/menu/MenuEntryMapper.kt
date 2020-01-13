package com.escodro.memory.mapper.menu

import com.escodro.memory.model.menu.MenuEntry as MemoryMenuEntry
import com.escodro.repository.model.menu.MenuEntry as RepoMenuEntry

/**
 * Maps Menu Entry between Memory and Repository.
 */
internal class MenuEntryMapper(private val menuOrderMapper: MenuOrderMapper) {

    /**
     * Maps Menu Entry from Memory to Repository.
     *
     * @param memoryEntryList the list of Menu Entry to be converted.
     *
     * @return the converted list of Menu Entry
     */
    fun toRepo(memoryEntryList: List<MemoryMenuEntry>): List<RepoMenuEntry> =
        memoryEntryList.map { toRepo(it) }

    private fun toRepo(memoryEntry: MemoryMenuEntry): RepoMenuEntry =
        RepoMenuEntry(
            groupId = memoryEntry.groupId,
            itemId = memoryEntry.itemId,
            order = menuOrderMapper.toRepo(memoryEntry.order),
            stringRes = memoryEntry.stringRes
        )
}
