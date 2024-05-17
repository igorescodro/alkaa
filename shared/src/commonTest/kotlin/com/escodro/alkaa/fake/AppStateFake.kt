package com.escodro.alkaa.fake

import com.escodro.appstate.AppState

internal class AppStateFake : AppState {
    override val shouldShowBottomBar: Boolean = true
    override val shouldShowNavRail: Boolean = false
}
