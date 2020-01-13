package com.escodro.repository.mapper.menu

import com.escodro.domain.model.menu.MenuOrder as DomainMenuOrder
import com.escodro.repository.model.menu.MenuOrder as RepoMenuOrder
import com.escodro.repository.model.menu.MenuOrder.FIRST
import com.escodro.repository.model.menu.MenuOrder.NONE

/**
 * Maps Menu Order between Repository and Domain.
 */
internal class MenuOrderMapper {

    /**
     * Maps Menu Order from Repo to Domain.
     *
     * @param repoOrder the Menu Order to be converted.
     *
     * @return the converted Menu Order
     */
    fun toDomain(repoOrder: RepoMenuOrder): DomainMenuOrder =
        when (repoOrder) {
            NONE -> DomainMenuOrder.NONE
            FIRST -> DomainMenuOrder.FIRST
        }
}
