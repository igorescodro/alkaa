package com.escodro.alkaa.presentation

import androidx.lifecycle.ViewModel
import com.escodro.alkaa.presentation.mapper.AppThemeOptionsMapper
import com.escodro.alkaa.presentation.model.AppThemeOptions
import com.escodro.domain.usecase.preferences.LoadAppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class MainViewModel(
    private val loadAppTheme: LoadAppTheme,
    private val mapper: AppThemeOptionsMapper
) : ViewModel() {

    fun loadCurrentTheme(): Flow<AppThemeOptions> = loadAppTheme().map { mapper.toViewData(it) }
}
