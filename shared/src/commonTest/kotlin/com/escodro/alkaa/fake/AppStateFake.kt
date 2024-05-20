package com.escodro.alkaa.fake

import com.escodro.appstate.AppState
import com.escodro.parcelable.CommonParcelize

@CommonParcelize
internal class AppStateFake : AppState {
    override val shouldShowBottomBar: Boolean = true
    override val shouldShowNavRail: Boolean = false
}
