package com.escodro.repository.mapper.menu

import com.escodro.domain.model.menu.MenuEntry as DomainMenuEntry
import com.escodro.repository.model.menu.MenuEntry as RepoMenuEntry

/**
 * Maps Menu Entry between Repository and Domain.
 */
internal class MenuEntryMapper(private val menuOrderMapper: MenuOrderMapper) {

    /**
     * Maps Menu Entry from Repo to Domain.
     *
     * @param repoEntryList the list of Menu Entry to be converted.
     *
     * @return the converted list of Menu Entry
     */
    fun toDomain(repoEntryList: List<RepoMenuEntry>): List<DomainMenuEntry> =
        repoEntryList.map { toDomain(it) }

    private fun toDomain(repoEntry: RepoMenuEntry): DomainMenuEntry =
        DomainMenuEntry(
            groupId = repoEntry.groupId,
            itemId = repoEntry.itemId,
            order = menuOrderMapper.toDomain(repoEntry.order),
            stringRes = repoEntry.stringRes
        )
}
