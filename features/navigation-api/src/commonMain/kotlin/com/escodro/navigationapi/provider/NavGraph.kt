package com.escodro.navigationapi.provider

import androidx.navigation3.runtime.EntryProviderScope
import com.escodro.navigationapi.controller.NavEventController
import com.escodro.navigationapi.destination.Destination

interface NavGraph {

    val navGraph: EntryProviderScope<Destination>.(NavEventController) -> Unit
}
