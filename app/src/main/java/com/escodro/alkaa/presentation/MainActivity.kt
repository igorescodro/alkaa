package com.escodro.alkaa.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.escodro.shared.AlkaaMultiplatformApp

/**
 * Main Alkaa Activity.
 */
internal class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AlkaaMultiplatformApp()
        }
    }

    // private fun updateTheme(darkTheme: Boolean) {
    //     window.apply {
    //         statusBarColor = if (darkTheme) Color.BLACK else Color.WHITE
    //         navigationBarColor = if (darkTheme) Color.BLACK else Color.WHITE
    //         WindowInsetsControllerCompat(this, this.decorView).isAppearanceLightStatusBars =
    //             !darkTheme
    //     }
    // }
    //
    // @Composable
    // private fun rememberIsDarkTheme(viewModel: MainViewModel = getViewModel()): Boolean {
    //     val isSystemDarkTheme = isSystemInDarkTheme()
    //
    //     val theme by remember(viewModel) {
    //         viewModel.loadCurrentTheme()
    //     }.collectAsState(initial = AppThemeOptions.SYSTEM)
    //
    //     val isDarkTheme = when (theme) {
    //         AppThemeOptions.LIGHT -> false
    //         AppThemeOptions.DARK -> true
    //         AppThemeOptions.SYSTEM -> isSystemDarkTheme
    //     }
    //     return isDarkTheme
    // }
}
