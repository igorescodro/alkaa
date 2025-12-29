package com.escodro.navigationapi.controller

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.escodro.navigationapi.destination.Destination
import com.escodro.navigationapi.destination.TopAppBarVisibleDestinations

/**
 * Controller to manage the navigation back stack.
 *
 * @param T the type of [Destination] managed in the back stack
 * @param startDestination the starting destination
 */
class NavBackStack<T : Destination>(startDestination: T) {

    private val topLevelStacks: LinkedHashMap<T, SnapshotStateList<T>> = linkedMapOf(
        startDestination to mutableStateListOf(startDestination),
    )

    var topLevelKey: T by mutableStateOf(startDestination)
        private set

    val backStack: SnapshotStateList<T> = mutableStateListOf(startDestination)

    var isTopBarVisible: Boolean by mutableStateOf(true)
        private set

    private fun updateBackStack() =
        backStack.apply {
            clear()
            addAll(topLevelStacks.flatMap { it.value })
        }

    @Suppress("NestedScopeFunctions")
    fun addTopLevel(destination: T) {
        // If the top level doesn't exist, add it
        if (topLevelStacks[destination] == null) {
            topLevelStacks[destination] = mutableStateListOf(destination)
        } else {
            // Otherwise just move it to the end of the stacks
            topLevelStacks.apply {
                remove(destination)?.let {
                    put(destination, it)
                }
            }
        }
        topLevelKey = destination
        isTopBarVisible = true
        updateBackStack()
    }

    fun clearTopLevel(destination: T) {
        topLevelStacks[destination]?.clear()
        topLevelStacks[destination]?.add(destination)
        if (topLevelKey == destination) {
            updateTopAppBarVisibility(destination)
            updateBackStack()
        }
    }

    fun add(destination: T) {
        topLevelStacks[topLevelKey]?.add(destination)
        updateTopAppBarVisibility(destination)
        updateBackStack()
    }

    fun removeLast() {
        val removedKey = topLevelStacks[topLevelKey]?.removeLastOrNull()
        // If the removed key was a top level key, remove the associated top level stack
        topLevelStacks.remove(removedKey)
        topLevelKey = topLevelStacks.keys.last()

        val previousTopLevelStack: T? = topLevelStacks[topLevelKey]?.lastOrNull()
        previousTopLevelStack?.let { destination -> updateTopAppBarVisibility(destination) }

        updateBackStack()
    }

    private fun updateTopAppBarVisibility(destination: T) {
        isTopBarVisible =
            TopAppBarVisibleDestinations.any { dest -> dest::class == destination::class }
    }
}
