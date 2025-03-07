package com.escodro.preference.provider

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.core.net.toUri
import com.escodro.splitinstall.LoadFeature

internal class AndroidTrackerProvider(private val context: Context) : TrackerProvider {

    @Composable
    override fun Content(onUpPress: () -> Unit) {
        LoadFeature(
            context = context,
            featureName = FeatureTracker,
            onDismiss = onUpPress,
        ) {
            // Workaround to be able to use Dynamic Feature with Compose
            // https://issuetracker.google.com/issues/183677219
            val intent = Intent(Intent.ACTION_VIEW).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                data = TrackerDeepLink.toUri()
                `package` = context.packageName
            }
            context.startActivity(intent)
        }
    }

    private companion object {
        private const val FeatureTracker = "tracker"
        private const val TrackerDeepLink = "app://com.escodro.tracker"
    }
}
