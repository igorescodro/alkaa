package com.escodro.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.atomicfu.locks.SynchronizedObject
import kotlinx.atomicfu.locks.synchronized
import okio.Path.Companion.toPath

private lateinit var dataStore: DataStore<Preferences>

private val lock = SynchronizedObject()

/**
 * Gets the [DataStore] instance.
 *
 * @param producePath function to produce the path to the data store file
 *
 * @return the [DataStore] instance
 */
fun getDataStore(producePath: () -> String): DataStore<Preferences> =
    synchronized(lock) {
        if (::dataStore.isInitialized) {
            dataStore
        } else {
            PreferenceDataStoreFactory.createWithPath(
                produceFile = { producePath().toPath() },
            ).also { dataStore = it }
        }
    }

internal const val dataStoreFileName = "alkaa_settings.preferences_pb"
