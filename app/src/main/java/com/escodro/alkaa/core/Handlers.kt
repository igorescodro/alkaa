package com.escodro.alkaa.core

import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * Handles the back pressed action.
 *
 * @param onBackPressed function to be called when the back is pressed
 */
@Composable
fun BackPressHandler(onBackPressed: () -> Unit) {
    val currentOnBackPressed by rememberUpdatedState(onBackPressed)

    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                currentOnBackPressed()
            }
        }
    }

    val backDispatcher = LocalBackPressedDispatcher.current

    DisposableEffect(backDispatcher) {
        backDispatcher.addCallback(backCallback)
        onDispose { backCallback.remove() }
    }
}

/**
 * Provides a [OnBackPressedDispatcher] that can be used by Android applications.
 */
val LocalBackPressedDispatcher =
    staticCompositionLocalOf<OnBackPressedDispatcher> { error("No Back Dispatcher provided") }
