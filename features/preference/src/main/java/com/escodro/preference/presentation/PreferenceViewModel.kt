package com.escodro.preference.presentation

import androidx.lifecycle.ViewModel
import com.escodro.core.coroutines.AppCoroutineScope
import com.escodro.domain.usecase.preferences.LoadAppTheme
import com.escodro.domain.usecase.preferences.UpdateAppTheme
import com.escodro.preference.AppThemeOptionsMapper
import com.escodro.preference.model.AppThemeOptions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class PreferenceViewModel(
    private val updateThemeUseCase: UpdateAppTheme,
    private val loadAppTheme: LoadAppTheme,
    private val applicationScope: AppCoroutineScope,
    private val mapper: AppThemeOptionsMapper,
) : ViewModel() {

    fun loadCurrentTheme(): Flow<AppThemeOptions> = loadAppTheme().map { mapper.toViewData(it) }

    fun updateTheme(theme: AppThemeOptions) = applicationScope.launch {
        val updatedTheme = mapper.toDomain(theme)
        updateThemeUseCase(updatedTheme)
    }
}
