package com.escodro.navigationapi.controller

import com.escodro.navigationapi.destination.Destination
import com.escodro.navigationapi.event.Event
import kotlinx.coroutines.flow.SharedFlow

/**
 * Controller responsible to handle the navigation events.
 */
interface NavEventController {

    /**
     * Flow to observe the destination changes.
     */
    val destinationState: SharedFlow<Destination>

    /**
     * Sends the event to the controller.
     *
     * @param event the event to be sent
     */
    fun sendEvent(event: Event)
}
