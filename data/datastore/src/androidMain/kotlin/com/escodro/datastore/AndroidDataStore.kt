package com.escodro.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

internal class AndroidDataStore(private val context: Context) {
    fun getDataStore(): DataStore<Preferences> = getDataStore(
        producePath = { context.filesDir.resolve(dataStoreFileName).absolutePath },
    )
}
