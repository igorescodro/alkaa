package com.escodro.alkaa.presentation.home

import com.escodro.alkaa.model.HomeSection

internal data class HomeActions(
    val onTaskClicked: (Long) -> Unit = {},
    val onAboutClicked: () -> Unit = {},
    val setCurrentSection: (HomeSection) -> Unit = {}
)
