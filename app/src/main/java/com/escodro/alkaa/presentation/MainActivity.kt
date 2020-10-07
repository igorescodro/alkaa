package com.escodro.alkaa.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.setContent
import com.escodro.alkaa.presentation.home.Home
import com.escodro.theme.AlkaaTheme

/**
 * Main Alkaa Activity.
 */
internal class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AlkaaTheme {
                Home()
            }
        }
    }
}
