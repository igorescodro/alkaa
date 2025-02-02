package com.escodro.navigation.controller

import com.escodro.coroutines.AppCoroutineScope
import com.escodro.navigationapi.controller.NavEventController
import com.escodro.navigationapi.destination.Destination
import com.escodro.navigationapi.event.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn

internal class NavEventControllerImpl(
    private val appCoroutineScope: AppCoroutineScope,
) : NavEventController {

    private val navigationEventState: MutableSharedFlow<Event> = MutableSharedFlow()

    override val destinationState: SharedFlow<Destination> =
        navigationEventState
            .map { event ->
                event.nextDestination()
            }.shareIn(
                scope = CoroutineScope(appCoroutineScope.context),
                started = SharingStarted.WhileSubscribed(),
            )

    override fun sendEvent(event: Event) {
        appCoroutineScope.launch {
            navigationEventState.emit(event)
        }
    }
}
