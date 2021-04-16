package com.escodro.alkaa.presentation.home

import com.escodro.alkaa.model.HomeSection

internal data class HomeActions(
    val onTaskClick: (Long) -> Unit = {},
    val onAboutClick: () -> Unit = {},
    val onTrackerClick: () -> Unit,
    val setCurrentSection: (HomeSection) -> Unit = {}
)
