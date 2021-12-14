package com.escodro.preference

import com.escodro.domain.model.AppThemeOptions as DomainThemeOptions
import com.escodro.preference.model.AppThemeOptions as ViewDataThemeOptions

/**
 * Maps AppThemeOptions between Repository and DataStore.
 */
internal class AppThemeOptionsMapper {

    /**
     * Maps AppThemeOptions from Domain to ViewData.
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

    /**
     * Maps AppThemeOptions from ViewData to Domain.
     *
     * @param appThemeOptions the object to be converted
     *
     * @return the converted object
     */
    fun toDomain(appThemeOptions: ViewDataThemeOptions): DomainThemeOptions =
        when (appThemeOptions) {
            ViewDataThemeOptions.LIGHT -> DomainThemeOptions.LIGHT
            ViewDataThemeOptions.DARK -> DomainThemeOptions.DARK
            ViewDataThemeOptions.SYSTEM -> DomainThemeOptions.SYSTEM
        }
}
