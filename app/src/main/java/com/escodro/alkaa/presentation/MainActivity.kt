package com.escodro.alkaa.presentation

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.core.view.WindowInsetsControllerCompat
import com.escodro.alkaa.navigation.NavGraph
import com.escodro.alkaa.presentation.model.AppThemeOptions
import com.escodro.designsystem.AlkaaTheme
import org.koin.androidx.viewmodel.ext.android.getViewModel

/**
 * Main Alkaa Activity.
 */
internal class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val isDarkTheme = rememberIsDarkTheme()
            updateTheme(isDarkTheme)

            AlkaaTheme(darkTheme = isDarkTheme) {
                NavGraph()
            }
        }
    }

    private fun updateTheme(darkTheme: Boolean) {
        window.apply {
            statusBarColor = if (darkTheme) Color.BLACK else Color.WHITE
            navigationBarColor = if (darkTheme) Color.BLACK else Color.WHITE
            WindowInsetsControllerCompat(this, this.decorView).isAppearanceLightStatusBars =
                !darkTheme
        }
    }

    @Composable
    private fun rememberIsDarkTheme(viewModel: MainViewModel = getViewModel()): Boolean {
        val isSystemDarkTheme = isSystemInDarkTheme()

        val theme by remember(viewModel) {
            viewModel.loadCurrentTheme()
        }.collectAsState(initial = AppThemeOptions.SYSTEM)

        val isDarkTheme = when (theme) {
            AppThemeOptions.LIGHT -> false
            AppThemeOptions.DARK -> true
            AppThemeOptions.SYSTEM -> isSystemDarkTheme
        }
        return isDarkTheme
    }
}
