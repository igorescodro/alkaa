package com.escodro.glance.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import androidx.datastore.dataStoreFile
import androidx.glance.state.GlanceStateDefinition
import com.escodro.glance.model.Task
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import java.io.File
import java.io.InputStream
import java.io.OutputStream

/**
 * Custom [GlanceStateDefinition] to store the widget-related data in a [DataStore].
 *
 * Ideally, all this data logic would stay in the `:data` layer, however once the processing here is
 * too specific for the Glance logic, I decided to keep it here. Moving some logic across other
 * layers would make it confusing and the use cases would need to know from which datasource the
 * data is available. There is no pretty solution for now.
 */
internal object TaskListStateDefinition : GlanceStateDefinition<ImmutableList<Task>> {

    private const val DATA_STORE_FILENAME = "taskList"

    private val Context.datastore by dataStore(DATA_STORE_FILENAME, TaskListSerializer)

    override suspend fun getDataStore(
        context: Context,
        fileKey: String,
    ): DataStore<ImmutableList<Task>> =
        context.datastore

    override fun getLocation(context: Context, fileKey: String): File =
        context.dataStoreFile(DATA_STORE_FILENAME)

    /**
     * Custom serializer to write and read data from [DataStore].
     */
    @OptIn(ExperimentalSerializationApi::class)
    object TaskListSerializer : Serializer<ImmutableList<Task>> {

        override val defaultValue: ImmutableList<Task>
            get() = persistentListOf()

        override suspend fun readFrom(input: InputStream): ImmutableList<Task> =
            Json.decodeFromStream(input)

        override suspend fun writeTo(t: ImmutableList<Task>, output: OutputStream) =
            Json.encodeToStream(t, output)
    }
}
