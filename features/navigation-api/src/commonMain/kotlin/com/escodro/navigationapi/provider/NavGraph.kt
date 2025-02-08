package com.escodro.navigationapi.provider

import androidx.navigation.NavGraphBuilder
import com.escodro.navigationapi.controller.NavEventController

interface NavGraph {

    val navGraph: NavGraphBuilder.(NavEventController) -> Unit
}
