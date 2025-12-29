package com.escodro.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.SynchronizedObject
import kotlinx.coroutines.internal.synchronized
import okio.Path.Companion.toPath

private var dataStore: DataStore<Preferences>? = null

@OptIn(InternalCoroutinesApi::class)
private val lock = SynchronizedObject()

/**
 * Gets the [DataStore] instance.
 *
 * @param producePath function to produce the path to the data store file
 *
 * @return the [DataStore] instance
 */
@OptIn(InternalCoroutinesApi::class)
fun getDataStore(producePath: () -> String): DataStore<Preferences> =
    synchronized(lock) {
        val instance: DataStore<Preferences>? = dataStore
        instance
            ?: PreferenceDataStoreFactory
                .createWithPath(produceFile = { producePath().toPath() })
                .also { dataStore = it }
    }

internal const val DataStoreFileName = "alkaa_settings.preferences_pb"
