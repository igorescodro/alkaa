package com.escodro.tracker.presentation

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.escodro.designsystem.AlkaaTheme
import com.escodro.tracker.di.injectDynamicFeature
import com.google.android.play.core.splitcompat.SplitCompat

internal class TrackerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injectDynamicFeature()

        setContent {
            AlkaaTheme {
                TrackerSection(onUpPress = { finish() })
            }
        }
    }

    override fun attachBaseContext(context: Context) {
        super.attachBaseContext(context)
        SplitCompat.installActivity(context)
    }
}
