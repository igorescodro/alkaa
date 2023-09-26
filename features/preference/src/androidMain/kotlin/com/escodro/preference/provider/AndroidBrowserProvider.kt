package com.escodro.preference.provider

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

internal class AndroidBrowserProvider(private val context: Context) : BrowserProvider {

    override fun openUrl(url: String) {
        with(Intent(Intent.ACTION_VIEW)) {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            data = url.toUri()
            context.startActivity(this)
        }
    }
}
