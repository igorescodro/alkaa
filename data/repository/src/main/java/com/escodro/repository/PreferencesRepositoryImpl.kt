package com.escodro.repository

import com.escodro.domain.model.AppThemeOptions
import com.escodro.domain.usecase.preferences.PreferencesRepository
import com.escodro.repository.datasource.PreferencesDataSource
import com.escodro.repository.mapper.AppThemeOptionsMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class PreferencesRepositoryImpl(
    private val dataSource: PreferencesDataSource,
    private val mapper: AppThemeOptionsMapper
) : PreferencesRepository {

    override suspend fun updateAppTheme(theme: AppThemeOptions) =
        dataSource.updateAppTheme(mapper.toRepo(theme))

    override fun loadAppTheme(): Flow<AppThemeOptions> =
        dataSource.loadAppTheme().map { mapper.toDomain(it) }
}
