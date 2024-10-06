package com.escodro.navigation.event

import com.escodro.navigation.destination.Destination
import com.escodro.navigation.destination.PreferenceDestination

object PreferenceEvent {

    data object OnAboutClick : Event {
        override fun nextDestination(): Destination = PreferenceDestination.About
    }

    data object OnLicensesClick : Event {
        override fun nextDestination(): Destination = PreferenceDestination.Licenses
    }

    data object OnTrackerClick : Event {
        override fun nextDestination(): Destination = PreferenceDestination.Tracker
    }
}
