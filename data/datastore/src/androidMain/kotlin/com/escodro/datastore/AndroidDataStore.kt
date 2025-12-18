package com.escodro.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

/**
 * Android implementation of [DataStore].
 */
internal class AndroidDataStore(private val context: Context) {

    /**
     * Gets the [DataStore] instance. It uses the same path as the Android DataStore, allowing to
     * use the same file when the user is updating the app.
     *
     * @return the [DataStore] instance
     */
    fun getDataStore(): DataStore<Preferences> = getDataStore(
        producePath = { context.filesDir.resolve("datastore/$DataStoreFileName").absolutePath },
    )
}
