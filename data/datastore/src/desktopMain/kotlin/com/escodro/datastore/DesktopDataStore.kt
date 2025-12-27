package com.escodro.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

internal class DesktopDataStore {

    fun getDataStore(): DataStore<Preferences> = getDataStore(
        producePath = { DataStoreFileName },
    )
}
