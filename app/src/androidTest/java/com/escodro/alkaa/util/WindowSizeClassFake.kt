package com.escodro.alkaa.util

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
internal object WindowSizeClassFake {

    val Phone = WindowSizeClass.calculateFromSize(DpSize(400.dp, 900.dp))
}
