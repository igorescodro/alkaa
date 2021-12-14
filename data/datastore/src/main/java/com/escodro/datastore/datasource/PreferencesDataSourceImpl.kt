package com.escodro.datastore.datasource

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.escodro.datastore.dataStore
import com.escodro.datastore.mapper.AppThemeOptionsMapper
import com.escodro.repository.datasource.PreferencesDataSource
import com.escodro.repository.model.AppThemeOptions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.escodro.datastore.model.AppThemeOptions as DataStoreThemeOptions

internal class PreferencesDataSourceImpl(
    private val context: Context,
    private val mapper: AppThemeOptionsMapper
) : PreferencesDataSource {

    override suspend fun updateAppTheme(theme: AppThemeOptions) {
        context.dataStore.edit { settings ->
            settings[APP_THEME_OPTION] = mapper.toDataStore(theme).id
        }
    }

    override fun loadAppTheme(): Flow<AppThemeOptions> =
        context.dataStore.data.map { preferences ->
            val id = preferences[APP_THEME_OPTION] ?: DataStoreThemeOptions.SYSTEM.id
            val result = DataStoreThemeOptions.values().find { it.id == id } ?: DataStoreThemeOptions.SYSTEM
            mapper.toRepo(result)
        }

    private companion object {
        val APP_THEME_OPTION = intPreferencesKey("alkaa_theme_option")
    }
}
