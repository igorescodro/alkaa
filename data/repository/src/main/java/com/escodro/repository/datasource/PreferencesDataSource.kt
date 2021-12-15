package com.escodro.repository.datasource

import com.escodro.repository.model.AppThemeOptions
import kotlinx.coroutines.flow.Flow

/**
 * Interface to represent the implementation of Preferences data source.
 */
interface PreferencesDataSource {

    /**
     * Updates the current app theme.
     *
     * @param theme the theme to be updated
     */
    suspend fun updateAppTheme(theme: AppThemeOptions)

    /**
     * Loads the current app theme.
     *
     * @return flow of [AppThemeOptions]
     */
    fun loadAppTheme(): Flow<AppThemeOptions>
}
