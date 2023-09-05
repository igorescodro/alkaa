package com.escodro.shared

import androidx.compose.runtime.Composable
import com.escodro.designsystem.AlkaaTheme
import com.escodro.home.presentation.Home

@Composable
fun AlkaaMultiplatformApp() {
    AlkaaTheme {
        HelloWorld()
    }
}

@Composable
private fun HelloWorld() {
    Home(
        onTaskClick = {},
        onAboutClick = {},
        onTrackerClick = {},
        onOpenSourceClick = {},
        onTaskSheetOpen = {},
        onCategorySheetOpen = {},
    )
}
