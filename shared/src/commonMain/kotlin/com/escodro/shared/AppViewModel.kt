package com.escodro.shared

import com.escodro.domain.usecase.preferences.LoadAppTheme
import com.escodro.shared.mapper.AppThemeOptionsMapper
import com.escodro.shared.model.AppThemeOptions
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class AppViewModel(
    private val loadAppTheme: LoadAppTheme,
    private val mapper: AppThemeOptionsMapper,
) : ViewModel() {

    fun loadCurrentTheme(): Flow<AppThemeOptions> = loadAppTheme().map { mapper.toViewData(it) }
}
