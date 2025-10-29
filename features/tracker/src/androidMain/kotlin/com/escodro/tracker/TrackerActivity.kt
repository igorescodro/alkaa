package com.escodro.tracker

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.escodro.designsystem.theme.AlkaaTheme
import com.escodro.tracker.presentation.TrackerScreen
import com.google.android.play.core.splitcompat.SplitCompat

internal class TrackerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AlkaaTheme {
                TrackerScreen(onUpPress = { finish() })
            }
        }
    }

    override fun attachBaseContext(context: Context) {
        super.attachBaseContext(context)
        SplitCompat.installActivity(context)
    }
}
