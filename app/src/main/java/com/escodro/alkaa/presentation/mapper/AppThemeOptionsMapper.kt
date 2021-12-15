package com.escodro.alkaa.presentation.mapper

import com.escodro.alkaa.presentation.model.AppThemeOptions as ViewDataThemeOptions
import com.escodro.domain.model.AppThemeOptions as DomainThemeOptions

/**
 * Maps AppThemeOptions between Repository and DataStore.
 */
internal class AppThemeOptionsMapper {

    /**
     * Maps AppThemeOptions from DataStore to Domain.
     *
     * @param appThemeOptions the object to be converted
     *
     * @return the converted object
     */
    fun toViewData(appThemeOptions: DomainThemeOptions): ViewDataThemeOptions =
        when (appThemeOptions) {
            DomainThemeOptions.LIGHT -> ViewDataThemeOptions.LIGHT
            DomainThemeOptions.DARK -> ViewDataThemeOptions.DARK
            DomainThemeOptions.SYSTEM -> ViewDataThemeOptions.SYSTEM
        }
}
