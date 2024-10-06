package com.escodro.navigation.controller

import com.escodro.coroutines.AppCoroutineScope
import com.escodro.navigation.destination.Destination
import com.escodro.navigation.event.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn

class NavEventController(private val appCoroutineScope: AppCoroutineScope) {

    private val navigationEventState: MutableSharedFlow<Event> = MutableSharedFlow()

    val destinationState: SharedFlow<Destination> =
        navigationEventState
            .map { event ->
                event.nextDestination()
            }.shareIn(
                scope = CoroutineScope(appCoroutineScope.context),
                started = SharingStarted.WhileSubscribed(),
            )

    fun sendEvent(event: Event) {
        appCoroutineScope.launch {
            navigationEventState.emit(event)
        }
    }
}
