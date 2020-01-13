package com.escodro.memory.mapper.menu

import com.escodro.memory.model.menu.MenuOrder as MemoryMenuOrder
import com.escodro.memory.model.menu.MenuOrder.FIRST
import com.escodro.memory.model.menu.MenuOrder.NONE
import com.escodro.repository.model.menu.MenuOrder as RepoMenuOrder

/**
 * Maps Menu Order between Memory and Repository.
 */
internal class MenuOrderMapper {

    /**
     * Maps Menu Order from Memory to Repository.
     *
     * @param memoryOrder the Menu Order to be converted.
     *
     * @return the converted Menu Order
     */
    fun toRepo(memoryOrder: MemoryMenuOrder): RepoMenuOrder =
        when (memoryOrder) {
            NONE -> RepoMenuOrder.NONE
            FIRST -> RepoMenuOrder.FIRST
        }
}
