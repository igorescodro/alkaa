package com.escodro.alkaa

import com.escodro.appstate.AppState

internal class AppStateFake : AppState {
    override val shouldShowBottomBar: Boolean = true
    override val shouldShowNavRail: Boolean = false
}
